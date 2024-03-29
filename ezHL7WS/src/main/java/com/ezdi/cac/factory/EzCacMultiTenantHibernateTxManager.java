package com.ezdi.cac.factory;

import java.sql.Connection;

import javax.sql.DataSource;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Interceptor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.transaction.spi.TransactionContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.JdbcTransactionObjectSupport;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.IllegalTransactionStateException;
import org.springframework.transaction.InvalidIsolationLevelException;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class EzCacMultiTenantHibernateTxManager extends AbstractPlatformTransactionManager
		implements ResourceTransactionManager, BeanFactoryAware, InitializingBean
{

	private static final long serialVersionUID = -631286387845808614L;

	private SessionFactory sessionFactory;

	private SessionFactory tenantSessionFactory;

	private SessionFactory tempSessionFactory;

	public SessionFactory getTempSessionFactory()
	{
		return tempSessionFactory;
	}

	public void setTempSessionFactory(SessionFactory tempSessionFactory)
	{
		this.tempSessionFactory = tempSessionFactory;
	}

	private DataSource dataSource;

	private boolean autodetectDataSource = true;

	private boolean prepareConnection = true;

	private boolean hibernateManagedSession = false;

	private Object entityInterceptor;

	private boolean isTenant;

	public boolean getIsTenant()
	{
		return isTenant;
	}

	public void setIsTenant(boolean isTenant)
	{
		this.isTenant = isTenant;
	}

	/**
	 * Just needed for entityInterceptorBeanName.
	 * 
	 * @see #setEntityInterceptorBeanName
	 */
	private BeanFactory beanFactory;

	/**
	 * Create a new HibernateTransactionManager instance. A SessionFactory has to be set to be able
	 * to use it.
	 * 
	 * @see #setSessionFactory
	 */
	public EzCacMultiTenantHibernateTxManager()
	{
		super();
	}

	/**
	 * Create a new HibernateTransactionManager instance.
	 * 
	 * @param sessionFactory
	 *            SessionFactory to manage transactions for
	 */
	public EzCacMultiTenantHibernateTxManager(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
		afterPropertiesSet();
	}

	/**
	 * Set the SessionFactory that this instance should manage transactions for.
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Return the SessionFactory that this instance should manage transactions for.
	 */
	public SessionFactory getSessionFactory()
	{
		return this.sessionFactory;
	}

	/**
	 * Set the SessionFactory that this instance should manage transactions for.
	 */
	public void setTenantSessionFactory(SessionFactory sessionFactory)
	{
		this.tenantSessionFactory = sessionFactory;
	}

	/**
	 * Return the SessionFactory that this instance should manage transactions for.
	 */
	public SessionFactory getTenantSessionFactory()
	{
		return this.tenantSessionFactory;
	}

	/**
	 * Set the JDBC DataSource that this instance should manage transactions for. The DataSource
	 * should match the one used by the Hibernate SessionFactory: for example, you could specify the
	 * same JNDI DataSource for both.
	 * <p>
	 * If the SessionFactory was configured with LocalDataSourceConnectionProvider, i.e. by Spring's
	 * LocalSessionFactoryBean with a specified "dataSource", the DataSource will be auto-detected:
	 * You can still explicitly specify the DataSource, but you don't need to in this case.
	 * <p>
	 * A transactional JDBC Connection for this DataSource will be provided to application code
	 * accessing this DataSource directly via DataSourceUtils or JdbcTemplate. The Connection will
	 * be taken from the Hibernate Session.
	 * <p>
	 * The DataSource specified here should be the target DataSource to manage transactions for, not
	 * a TransactionAwareDataSourceProxy. Only data access code may work with
	 * TransactionAwareDataSourceProxy, while the transaction manager needs to work on the
	 * underlying target DataSource. If there's nevertheless a TransactionAwareDataSourceProxy
	 * passed in, it will be unwrapped to extract its target DataSource.
	 * 
	 * @see #setAutodetectDataSource
	 * @see org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
	 * @see org.springframework.jdbc.datasource.DataSourceUtils
	 * @see org.springframework.jdbc.core.JdbcTemplate
	 */
	public void setDataSource(DataSource dataSource)
	{
		if (dataSource instanceof TransactionAwareDataSourceProxy)
		{
			// If we got a TransactionAwareDataSourceProxy, we need to perform transactions
			// for its underlying target DataSource, else data access code won't see
			// properly exposed transactions (i.e. transactions for the target DataSource).
			this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
		} else
		{
			this.dataSource = dataSource;
		}
	}

	/**
	 * Return the JDBC DataSource that this instance manages transactions for.
	 */
	public DataSource getDataSource()
	{
		return this.dataSource;
	}

	/**
	 * Set whether to autodetect a JDBC DataSource used by the Hibernate SessionFactory, if set via
	 * LocalSessionFactoryBean's {@code setDataSource}. Default is "true".
	 * <p>
	 * Can be turned off to deliberately ignore an available DataSource, in order to not expose
	 * Hibernate transactions as JDBC transactions for that DataSource.
	 * 
	 * @see #setDataSource
	 */
	public void setAutodetectDataSource(boolean autodetectDataSource)
	{
		this.autodetectDataSource = autodetectDataSource;
	}

	/**
	 * Set whether to prepare the underlying JDBC Connection of a transactional Hibernate Session,
	 * that is, whether to apply a transaction-specific isolation level and/or the transaction's
	 * read-only flag to the underlying JDBC Connection.
	 * <p>
	 * Default is "true". If you turn this flag off, the transaction manager will not support
	 * per-transaction isolation levels anymore. It will not call
	 * {@code Connection.setReadOnly(true)} for read-only transactions anymore either. If this flag
	 * is turned off, no cleanup of a JDBC Connection is required after a transaction, since no
	 * Connection settings will get modified.
	 * 
	 * @see java.sql.Connection#setTransactionIsolation
	 * @see java.sql.Connection#setReadOnly
	 */
	public void setPrepareConnection(boolean prepareConnection)
	{
		this.prepareConnection = prepareConnection;
	}

	/**
	 * Set whether to operate on a Hibernate-managed Session instead of a Spring-managed Session,
	 * that is, whether to obtain the Session through Hibernate's
	 * {@link org.hibernate.SessionFactory#getCurrentSession()} instead of
	 * {@link org.hibernate.SessionFactory#openSession()} (with a Spring
	 * {@link org.springframework.transaction.support.TransactionSynchronizationManager} check
	 * preceding it).
	 * <p>
	 * Default is "false", i.e. using a Spring-managed Session: taking the current thread-bound
	 * Session if available (e.g. in an Open-Session-in-View scenario), creating a new Session for
	 * the current transaction otherwise.
	 * <p>
	 * Switch this flag to "true" in order to enforce use of a Hibernate-managed Session. Note that
	 * this requires {@link org.hibernate.SessionFactory#getCurrentSession()} to always return a
	 * proper Session when called for a Spring-managed transaction; transaction begin will fail if
	 * the {@code getCurrentSession()} call fails.
	 * <p>
	 * This mode will typically be used in combination with a custom Hibernate
	 * {@link org.hibernate.context.spi.CurrentSessionContext} implementation that stores Sessions
	 * in a place other than Spring's TransactionSynchronizationManager. It may also be used in
	 * combination with Spring's Open-Session-in-View support (using Spring's default
	 * {@link org.springframework.orm.hibernate4.SpringSessionContext}), in which case it subtly
	 * differs from the Spring-managed Session mode: The pre-bound Session will <i>not</i> receive a
	 * {@code clear()} call (on rollback) or a {@code disconnect()} call (on transaction completion)
	 * in such a scenario; this is rather left up to a custom CurrentSessionContext implementation
	 * (if desired).
	 */
	public void setHibernateManagedSession(boolean hibernateManagedSession)
	{
		this.hibernateManagedSession = hibernateManagedSession;
	}

	/**
	 * Set the bean name of a Hibernate entity interceptor that allows to inspect and change
	 * property values before writing to and reading from the database. Will get applied to any new
	 * Session created by this transaction manager.
	 * <p>
	 * Requires the bean factory to be known, to be able to resolve the bean name to an interceptor
	 * instance on session creation. Typically used for prototype interceptors, i.e. a new
	 * interceptor instance per session.
	 * <p>
	 * Can also be used for shared interceptor instances, but it is recommended to set the
	 * interceptor reference directly in such a scenario.
	 * 
	 * @param entityInterceptorBeanName
	 *            the name of the entity interceptor in the bean factory
	 * @see #setBeanFactory
	 * @see #setEntityInterceptor
	 */
	public void setEntityInterceptorBeanName(String entityInterceptorBeanName)
	{
		this.entityInterceptor = entityInterceptorBeanName;
	}

	/**
	 * Set a Hibernate entity interceptor that allows to inspect and change property values before
	 * writing to and reading from the database. Will get applied to any new Session created by this
	 * transaction manager.
	 * <p>
	 * Such an interceptor can either be set at the SessionFactory level, i.e. on
	 * LocalSessionFactoryBean, or at the Session level, i.e. on HibernateTransactionManager.
	 * 
	 * @see org.springframework.orm.hibernate4.LocalSessionFactoryBean#setEntityInterceptor
	 */
	public void setEntityInterceptor(Interceptor entityInterceptor)
	{
		this.entityInterceptor = entityInterceptor;
	}

	/**
	 * Return the current Hibernate entity interceptor, or {@code null} if none. Resolves an entity
	 * interceptor bean name via the bean factory, if necessary.
	 * 
	 * @throws IllegalStateException
	 *             if bean name specified but no bean factory set
	 * @throws org.springframework.beans.BeansException
	 *             if bean name resolution via the bean factory failed
	 * @see #setEntityInterceptor
	 * @see #setEntityInterceptorBeanName
	 * @see #setBeanFactory
	 */
	public Interceptor getEntityInterceptor() throws IllegalStateException, BeansException
	{
		if (this.entityInterceptor instanceof Interceptor)
		{
			return (Interceptor) entityInterceptor;
		} else if (this.entityInterceptor instanceof String)
		{
			if (this.beanFactory == null)
			{
				throw new IllegalStateException(
						"Cannot get entity interceptor via bean name if no bean factory set");
			}
			String beanName = (String) this.entityInterceptor;
			return this.beanFactory.getBean(beanName, Interceptor.class);
		} else
		{
			return null;
		}
	}

	/**
	 * The bean factory just needs to be known for resolving entity interceptor bean names. It does
	 * not need to be set for any other mode of operation.
	 * 
	 * @see #setEntityInterceptorBeanName
	 */
	@Override
	public void setBeanFactory(BeanFactory beanFactory)
	{
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet()
	{
		if (getSessionFactory() == null)
		{
			throw new IllegalArgumentException("Property 'sessionFactory' is required");
		}
		if (this.entityInterceptor instanceof String && this.beanFactory == null)
		{
			throw new IllegalArgumentException(
					"Property 'beanFactory' is required for 'entityInterceptorBeanName'");
		}

		// Check for SessionFactory's DataSource.
		if (this.autodetectDataSource && getDataSource() == null)
		{
			DataSource sfds = SessionFactoryUtils.getDataSource(getSessionFactory());
			if (sfds != null)
			{
				// Use the SessionFactory's DataSource for exposing transactions to JDBC code.
				if (logger.isInfoEnabled())
				{
					logger.info("Using DataSource [" + sfds
							+ "] of Hibernate SessionFactory for HibernateTransactionManager");
				}
				setDataSource(sfds);
			}
		}
	}

	@Override
	public Object getResourceFactory()
	{
		return getSessionFactory();
	}

	@Override
	protected Object doGetTransaction()
	{
		HibernateTransactionObject txObject = new HibernateTransactionObject();
		txObject.setSavepointAllowed(isNestedTransactionAllowed());

		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
				.getResource(getTempSessionFactory());
		if (sessionHolder != null)
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Found thread-bound Session [" + sessionHolder.getSession()
						+ "] for Hibernate transaction");
			}
			txObject.setSessionHolder(sessionHolder);
		} else if (this.hibernateManagedSession)
		{
			try
			{
				Session session = this.sessionFactory.getCurrentSession();
				if (logger.isDebugEnabled())
				{
					logger.debug("Found Hibernate-managed Session [" + session
							+ "] for Spring-managed transaction");
				}
				txObject.setExistingSession(session);
			} catch (HibernateException ex)
			{
				throw new DataAccessResourceFailureException(
						"Could not obtain Hibernate-managed Session for Spring-managed transaction",
						ex);
			}
		}

		if (getDataSource() != null)
		{
			ConnectionHolder conHolder = (ConnectionHolder) TransactionSynchronizationManager
					.getResource(getDataSource());
			txObject.setConnectionHolder(conHolder);
		}

		return txObject;
	}

	@Override
	protected boolean isExistingTransaction(Object transaction)
	{
		HibernateTransactionObject txObject = (HibernateTransactionObject) transaction;
		return (txObject.hasSpringManagedTransaction() || (this.hibernateManagedSession && txObject
				.hasHibernateManagedTransaction()));
	}

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition)
	{
		HibernateTransactionObject txObject = (HibernateTransactionObject) transaction;

		if (txObject.hasConnectionHolder()
				&& !txObject.getConnectionHolder().isSynchronizedWithTransaction())
		{
			throw new IllegalTransactionStateException(
					"Pre-bound JDBC Connection found! HibernateTransactionManager does not support "
							+ "running within DataSourceTransactionManager if told to manage the DataSource itself. "
							+ "It is recommended to use a single HibernateTransactionManager for all transactions "
							+ "on a single DataSource, no matter whether Hibernate or JDBC access.");
		}

		Session session = null;

		try
		{
			if (txObject.getSessionHolder() == null
					|| txObject.getSessionHolder().isSynchronizedWithTransaction())
			{
				Interceptor entityInterceptor = getEntityInterceptor();

				Session newSession;/*
									 * = (entityInterceptor != null ?
									 * getSessionFactory().withOptions
									 * ().interceptor(entityInterceptor).openSession() :
									 * getSessionFactory().openSession());
									 */

				// multi-tenant management

				/*
				 * if (this.getIsTenant()) {
				 * 
				 * setTempSessionFactory(tenantSessionFactory);//getSessionFactory(); newSession =
				 * getTempSessionFactory
				 * ().withOptions().interceptor(entityInterceptor).openSession();
				 * 
				 * } else { setTempSessionFactory(sessionFactory);
				 * 
				 * }
				 */
				newSession = (entityInterceptor != null ? getTempSessionFactory().withOptions()
						.interceptor(entityInterceptor).openSession() : getTempSessionFactory()
						.openSession());

				// end multi-tenant management

				if (logger.isDebugEnabled())
				{
					logger.debug("Opened new Session [" + newSession
							+ "] for Hibernate transaction");
				}
				txObject.setSession(newSession);
				// SessionHolderThreadLocal.setSession(newSession);
			}

			session = txObject.getSessionHolder().getSession();

			if (this.prepareConnection && isSameConnectionForEntireSession(session))
			{
				// We're allowed to change the transaction settings of the JDBC Connection.
				if (logger.isDebugEnabled())
				{
					logger.debug("Preparing JDBC Connection of Hibernate Session [" + session + "]");
				}
				Connection con = ((SessionImplementor) session).connection();
				Integer previousIsolationLevel = DataSourceUtils.prepareConnectionForTransaction(
						con, definition);
				txObject.setPreviousIsolationLevel(previousIsolationLevel);
			} else
			{
				// Not allowed to change the transaction settings of the JDBC Connection.
				if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT)
				{
					// We should set a specific isolation level but are not allowed to...
					throw new InvalidIsolationLevelException(
							"HibernateTransactionManager is not allowed to support custom isolation levels: "
									+ "make sure that its 'prepareConnection' flag is on (the default) and that the "
									+ "Hibernate connection release mode is set to 'on_close' (SpringTransactionFactory's default).");
				}
				if (logger.isDebugEnabled())
				{
					logger.debug("Not preparing JDBC Connection of Hibernate Session [" + session
							+ "]");
				}
			}

			if (definition.isReadOnly() && txObject.isNewSession())
			{
				// Just set to MANUAL in case of a new Session for this transaction.
				session.setFlushMode(FlushMode.MANUAL);
			}

			if (!definition.isReadOnly() && !txObject.isNewSession())
			{
				// We need AUTO or COMMIT for a non-read-only transaction.
				FlushMode flushMode = session.getFlushMode();
				if (session.getFlushMode().equals(FlushMode.MANUAL))
				{
					session.setFlushMode(FlushMode.AUTO);
					txObject.getSessionHolder().setPreviousFlushMode(flushMode);
				}
			}

			Transaction hibTx;

			// Register transaction timeout.
			int timeout = determineTimeout(definition);
			if (timeout != TransactionDefinition.TIMEOUT_DEFAULT)
			{
				// Use Hibernate's own transaction timeout mechanism on Hibernate 3.1+
				// Applies to all statements, also to inserts, updates and deletes!
				hibTx = session.getTransaction();
				hibTx.setTimeout(timeout);
				hibTx.begin();
			} else
			{
				// Open a plain Hibernate transaction without specified timeout.
				hibTx = session.beginTransaction();
			}

			// Add the Hibernate transaction to the session holder.
			txObject.getSessionHolder().setTransaction(hibTx);

			// Register the Hibernate Session's JDBC Connection for the DataSource, if set.
			if (getDataSource() != null)
			{
				Connection con = ((SessionImplementor) session).connection();
				ConnectionHolder conHolder = new ConnectionHolder(con);
				if (timeout != TransactionDefinition.TIMEOUT_DEFAULT)
				{
					conHolder.setTimeoutInSeconds(timeout);
				}
				if (logger.isDebugEnabled())
				{
					logger.debug("Exposing Hibernate transaction as JDBC transaction [" + con + "]");
				}
				TransactionSynchronizationManager.bindResource(getDataSource(), conHolder);
				txObject.setConnectionHolder(conHolder);
			}

			// Bind the session holder to the thread.
			if (txObject.isNewSessionHolder())
			{
				TransactionSynchronizationManager.bindResource(getTempSessionFactory(),
						txObject.getSessionHolder());
			}
			txObject.getSessionHolder().setSynchronizedWithTransaction(true);
		}

		catch (Throwable ex)
		{
			if (txObject.isNewSession())
			{
				try
				{
					if (session.getTransaction().isActive())
					{
						session.getTransaction().rollback();
					}
				} catch (Throwable ex2)
				{
					logger.debug("Could not rollback Session after failed transaction begin", ex);
				} finally
				{
					SessionFactoryUtils.closeSession(session);
				}
			}
			throw new CannotCreateTransactionException(
					"Could not open Hibernate Session for transaction", ex);
		}
	}

	@Override
	protected Object doSuspend(Object transaction)
	{
		HibernateTransactionObject txObject = (HibernateTransactionObject) transaction;
		txObject.setSessionHolder(null);
		SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager
				.unbindResource(getTempSessionFactory());
		txObject.setConnectionHolder(null);
		ConnectionHolder connectionHolder = null;
		if (getDataSource() != null)
		{
			connectionHolder = (ConnectionHolder) TransactionSynchronizationManager
					.unbindResource(getDataSource());
		}
		return new SuspendedResourcesHolder(sessionHolder, connectionHolder);
	}

	@Override
	protected void doResume(Object transaction, Object suspendedResources)
	{
		SuspendedResourcesHolder resourcesHolder = (SuspendedResourcesHolder) suspendedResources;
		if (TransactionSynchronizationManager.hasResource(getTempSessionFactory()))
		{
			// From non-transactional code running in active transaction synchronization
			// -> can be safely removed, will be closed on transaction completion.
			TransactionSynchronizationManager.unbindResource(getTempSessionFactory());
		}
		TransactionSynchronizationManager.bindResource(getTempSessionFactory(),
				resourcesHolder.getSessionHolder());
		if (getDataSource() != null)
		{
			TransactionSynchronizationManager.bindResource(getDataSource(),
					resourcesHolder.getConnectionHolder());
		}
	}

	@Override
	protected void doCommit(DefaultTransactionStatus status)
	{
		HibernateTransactionObject txObject = (HibernateTransactionObject) status.getTransaction();
		if (status.isDebug())
		{
			logger.debug("Committing Hibernate transaction on Session ["
					+ txObject.getSessionHolder().getSession() + "]");
		}
		try
		{
			txObject.getSessionHolder().getTransaction().commit();
		} catch (org.hibernate.TransactionException ex)
		{
			// assumably from commit call to the underlying JDBC connection
			throw new TransactionSystemException("Could not commit Hibernate transaction", ex);
		} catch (HibernateException ex)
		{
			// assumably failed to flush changes to database
			throw convertHibernateAccessException(ex);
		}
	}

	@Override
	protected void doRollback(DefaultTransactionStatus status)
	{
		HibernateTransactionObject txObject = (HibernateTransactionObject) status.getTransaction();
		if (status.isDebug())
		{
			logger.debug("Rolling back Hibernate transaction on Session ["
					+ txObject.getSessionHolder().getSession() + "]");
		}
		try
		{
			txObject.getSessionHolder().getTransaction().rollback();
		} catch (org.hibernate.TransactionException ex)
		{
			throw new TransactionSystemException("Could not roll back Hibernate transaction", ex);
		} catch (HibernateException ex)
		{
			// Shouldn't really happen, as a rollback doesn't cause a flush.
			throw convertHibernateAccessException(ex);
		} finally
		{
			if (!txObject.isNewSession() && !this.hibernateManagedSession)
			{
				// Clear all pending inserts/updates/deletes in the Session.
				// Necessary for pre-bound Sessions, to avoid inconsistent state.
				txObject.getSessionHolder().getSession().clear();
			}
		}
	}

	@Override
	protected void doSetRollbackOnly(DefaultTransactionStatus status)
	{
		HibernateTransactionObject txObject = (HibernateTransactionObject) status.getTransaction();
		if (status.isDebug())
		{
			logger.debug("Setting Hibernate transaction on Session ["
					+ txObject.getSessionHolder().getSession() + "] rollback-only");
		}
		txObject.setRollbackOnly();
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction)
	{
		HibernateTransactionObject txObject = (HibernateTransactionObject) transaction;

		// Remove the session holder from the thread.
		if (txObject.isNewSessionHolder())
		{
			TransactionSynchronizationManager.unbindResource(getTempSessionFactory());
		}

		// Remove the JDBC connection holder from the thread, if exposed.
		if (getDataSource() != null)
		{
			TransactionSynchronizationManager.unbindResource(getDataSource());
		}

		Session session = txObject.getSessionHolder().getSession();
		if (this.prepareConnection && session.isConnected()
				&& isSameConnectionForEntireSession(session))
		{
			// We're running with connection release mode "on_close": We're able to reset
			// the isolation level and/or read-only flag of the JDBC Connection here.
			// Else, we need to rely on the connection pool to perform proper cleanup.
			try
			{
				Connection con = ((SessionImplementor) session).connection();
				DataSourceUtils.resetConnectionAfterTransaction(con,
						txObject.getPreviousIsolationLevel());
			} catch (HibernateException ex)
			{
				logger.debug("Could not access JDBC Connection of Hibernate Session", ex);
			}
		}

		if (txObject.isNewSession())
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Closing Hibernate Session [" + session + "] after transaction");
			}
			SessionFactoryUtils.closeSession(session);
		} else
		{
			if (logger.isDebugEnabled())
			{
				logger.debug("Not closing pre-bound Hibernate Session [" + session
						+ "] after transaction");
			}
			if (txObject.getSessionHolder().getPreviousFlushMode() != null)
			{
				session.setFlushMode(txObject.getSessionHolder().getPreviousFlushMode());
			}
			if (!this.hibernateManagedSession)
			{
				session.disconnect();
			}
		}
		txObject.getSessionHolder().clear();
	}

	/**
	 * Return whether the given Hibernate Session will always hold the same JDBC Connection. This is
	 * used to check whether the transaction manager can safely prepare and clean up the JDBC
	 * Connection used for a transaction.
	 * <p>
	 * The default implementation checks the Session's connection release mode to be "on_close".
	 * 
	 * @param session
	 *            the Hibernate Session to check
	 * @see org.hibernate.engine.transaction.spi.TransactionContext#getConnectionReleaseMode()
	 * @see org.hibernate.ConnectionReleaseMode#ON_CLOSE
	 */
	protected boolean isSameConnectionForEntireSession(Session session)
	{
		if (!(session instanceof TransactionContext))
		{
			// The best we can do is to assume we're safe.
			return true;
		}
		ConnectionReleaseMode releaseMode = ((TransactionContext) session)
				.getConnectionReleaseMode();
		return ConnectionReleaseMode.ON_CLOSE.equals(releaseMode);
	}

	/**
	 * Convert the given HibernateException to an appropriate exception from the
	 * {@code org.springframework.dao} hierarchy.
	 * <p>
	 * Will automatically apply a specified SQLExceptionTranslator to a Hibernate JDBCException,
	 * else rely on Hibernate's default translation.
	 * 
	 * @param ex
	 *            HibernateException that occurred
	 * @return a corresponding DataAccessException
	 * @see SessionFactoryUtils#convertHibernateAccessException
	 */
	protected DataAccessException convertHibernateAccessException(HibernateException ex)
	{
		return SessionFactoryUtils.convertHibernateAccessException(ex);
	}

	/**
	 * Hibernate transaction object, representing a SessionHolder. Used as transaction object by
	 * HibernateTransactionManager.
	 */
	private class HibernateTransactionObject extends JdbcTransactionObjectSupport
	{

		private SessionHolder sessionHolder;

		private boolean newSessionHolder;

		private boolean newSession;

		public void setSession(Session session)
		{
			this.sessionHolder = new SessionHolder(session);
			this.newSessionHolder = true;
			this.newSession = true;
		}

		public void setExistingSession(Session session)
		{
			this.sessionHolder = new SessionHolder(session);
			this.newSessionHolder = true;
			this.newSession = false;
		}

		public void setSessionHolder(SessionHolder sessionHolder)
		{
			this.sessionHolder = sessionHolder;
			this.newSessionHolder = false;
			this.newSession = false;
		}

		public SessionHolder getSessionHolder()
		{
			return this.sessionHolder;
		}

		public boolean isNewSessionHolder()
		{
			return this.newSessionHolder;
		}

		public boolean isNewSession()
		{
			return this.newSession;
		}

		public boolean hasSpringManagedTransaction()
		{
			return (this.sessionHolder != null && this.sessionHolder.getTransaction() != null);
		}

		public boolean hasHibernateManagedTransaction()
		{
			return (this.sessionHolder != null && this.sessionHolder.getSession().getTransaction()
					.isActive());
		}

		public void setRollbackOnly()
		{
			this.sessionHolder.setRollbackOnly();
			if (hasConnectionHolder())
			{
				getConnectionHolder().setRollbackOnly();
			}
		}

		@Override
		public boolean isRollbackOnly()
		{
			return this.sessionHolder.isRollbackOnly()
					|| (hasConnectionHolder() && getConnectionHolder().isRollbackOnly());
		}

		@Override
		public void flush()
		{
			try
			{
				this.sessionHolder.getSession().flush();
			} catch (HibernateException ex)
			{
				throw convertHibernateAccessException(ex);
			}
		}
	}

	/**
	 * Holder for suspended resources. Used internally by {@code doSuspend} and {@code doResume}.
	 */
	private static class SuspendedResourcesHolder
	{

		private final SessionHolder sessionHolder;

		private final ConnectionHolder connectionHolder;

		private SuspendedResourcesHolder(SessionHolder sessionHolder, ConnectionHolder conHolder)
		{
			this.sessionHolder = sessionHolder;
			this.connectionHolder = conHolder;
		}

		private SessionHolder getSessionHolder()
		{
			return this.sessionHolder;
		}

		private ConnectionHolder getConnectionHolder()
		{
			return this.connectionHolder;
		}
	}

}
