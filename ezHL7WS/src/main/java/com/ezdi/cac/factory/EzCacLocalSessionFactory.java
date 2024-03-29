package com.ezdi.cac.factory;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.Interceptor;
import org.hibernate.SessionFactory;
import org.hibernate.cache.spi.RegionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.NamingStrategy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class EzCacLocalSessionFactory extends HibernateExceptionTranslator implements FactoryBean<SessionFactory>, ResourceLoaderAware,
		InitializingBean, DisposableBean
{

	@Autowired
	private ApplicationContext applicationContext;

	private DataSource dataSource;

	private Resource[] configLocations;

	private String[] mappingResources;

	private Resource[] mappingLocations;

	private Resource[] cacheableMappingLocations;

	private Resource[] mappingJarLocations;

	private Resource[] mappingDirectoryLocations;

	private Interceptor entityInterceptor;

	private NamingStrategy namingStrategy;

	private Object jtaTransactionManager;

	private Object multiTenantConnectionProvider;

	private Object currentTenantIdentifierResolver;

	private RegionFactory cacheRegionFactory;

	private Properties hibernateProperties;

	private Class<?>[] annotatedClasses;

	private String[] annotatedPackages;

	private String[] packagesToScan;

	private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

	private Configuration configuration;

	private SessionFactory sessionFactory;

	/**
	 * Set the DataSource to be used by the SessionFactory. If set, this will override corresponding settings in Hibernate properties.
	 * <p>
	 * If this is set, the Hibernate settings should not define a connection provider to avoid meaningless double configuration.
	 */
	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	/**
	 * Set the location of a single Hibernate XML config file, for example as classpath resource "classpath:hibernate.cfg.xml".
	 * <p>
	 * Note: Can be omitted when all necessary properties and mapping resources are specified locally via this bean.
	 * 
	 * @see org.hibernate.cfg.Configuration#configure(java.net.URL)
	 */
	public void setConfigLocation(Resource configLocation)
	{
		this.configLocations = new Resource[] { configLocation };
	}

	/**
	 * Set the locations of multiple Hibernate XML config files, for example as classpath resources
	 * "classpath:hibernate.cfg.xml,classpath:extension.cfg.xml".
	 * <p>
	 * Note: Can be omitted when all necessary properties and mapping resources are specified locally via this bean.
	 * 
	 * @see org.hibernate.cfg.Configuration#configure(java.net.URL)
	 */
	public void setConfigLocations(Resource... configLocations)
	{
		this.configLocations = configLocations;
	}

	/**
	 * Set Hibernate mapping resources to be found in the class path, like "example.hbm.xml" or "mypackage/example.hbm.xml". Analogous to
	 * mapping entries in a Hibernate XML config file. Alternative to the more generic setMappingLocations method.
	 * <p>
	 * Can be used to add to mappings from a Hibernate XML config file, or to specify all mappings locally.
	 * 
	 * @see #setMappingLocations
	 * @see org.hibernate.cfg.Configuration#addResource
	 */
	public void setMappingResources(String... mappingResources)
	{
		this.mappingResources = mappingResources;
	}

	/**
	 * Set locations of Hibernate mapping files, for example as classpath resource "classpath:example.hbm.xml". Supports any resource
	 * location via Spring's resource abstraction, for example relative paths like "WEB-INF/mappings/example.hbm.xml" when running in an
	 * application context.
	 * <p>
	 * Can be used to add to mappings from a Hibernate XML config file, or to specify all mappings locally.
	 * 
	 * @see org.hibernate.cfg.Configuration#addInputStream
	 */
	public void setMappingLocations(Resource... mappingLocations)
	{
		this.mappingLocations = mappingLocations;
	}

	/**
	 * Set locations of cacheable Hibernate mapping files, for example as web app resource "/WEB-INF/mapping/example.hbm.xml". Supports any
	 * resource location via Spring's resource abstraction, as long as the resource can be resolved in the file system.
	 * <p>
	 * Can be used to add to mappings from a Hibernate XML config file, or to specify all mappings locally.
	 * 
	 * @see org.hibernate.cfg.Configuration#addCacheableFile(java.io.File)
	 */
	public void setCacheableMappingLocations(Resource... cacheableMappingLocations)
	{
		this.cacheableMappingLocations = cacheableMappingLocations;
	}

	/**
	 * Set locations of jar files that contain Hibernate mapping resources, like "WEB-INF/lib/example.hbm.jar".
	 * <p>
	 * Can be used to add to mappings from a Hibernate XML config file, or to specify all mappings locally.
	 * 
	 * @see org.hibernate.cfg.Configuration#addJar(java.io.File)
	 */
	public void setMappingJarLocations(Resource... mappingJarLocations)
	{
		this.mappingJarLocations = mappingJarLocations;
	}

	/**
	 * Set locations of directories that contain Hibernate mapping resources, like "WEB-INF/mappings".
	 * <p>
	 * Can be used to add to mappings from a Hibernate XML config file, or to specify all mappings locally.
	 * 
	 * @see org.hibernate.cfg.Configuration#addDirectory(java.io.File)
	 */
	public void setMappingDirectoryLocations(Resource... mappingDirectoryLocations)
	{
		this.mappingDirectoryLocations = mappingDirectoryLocations;
	}

	/**
	 * Set a Hibernate entity interceptor that allows to inspect and change property values before writing to and reading from the database.
	 * Will get applied to any new Session created by this factory.
	 * 
	 * @see org.hibernate.cfg.Configuration#setInterceptor
	 */
	public void setEntityInterceptor(Interceptor entityInterceptor)
	{
		this.entityInterceptor = entityInterceptor;
	}

	/**
	 * Set a Hibernate NamingStrategy for the SessionFactory, determining the physical column and table names given the info in the mapping
	 * document.
	 * 
	 * @see org.hibernate.cfg.Configuration#setNamingStrategy
	 */
	public void setNamingStrategy(NamingStrategy namingStrategy)
	{
		this.namingStrategy = namingStrategy;
	}

	/**
	 * Set the Spring {@link org.springframework.transaction.jta.JtaTransactionManager} or the JTA
	 * {@link javax.transaction.TransactionManager} to be used with Hibernate, if any. Implicitly sets up {@code JtaPlatform} and
	 * {@code CMTTransactionStrategy}.
	 * 
	 * @see LocalSessionFactoryBuilder#setJtaTransactionManager
	 */
	public void setJtaTransactionManager(Object jtaTransactionManager)
	{
		this.jtaTransactionManager = jtaTransactionManager;
	}

	/**
	 * Set a Hibernate 4.1/4.2/4.3 {@code MultiTenantConnectionProvider} to be passed on to the SessionFactory: as an instance, a Class, or
	 * a String class name.
	 * <p>
	 * Note that the package location of the {@code MultiTenantConnectionProvider} interface changed between Hibernate 4.2 and 4.3. This
	 * method accepts both variants.
	 * 
	 * @see LocalSessionFactoryBuilder#setMultiTenantConnectionProvider
	 */
	public void setMultiTenantConnectionProvider(Object multiTenantConnectionProvider)
	{
		this.multiTenantConnectionProvider = multiTenantConnectionProvider;
	}

	/**
	 * Set a Hibernate 4.1/4.2/4.3 {@code CurrentTenantIdentifierResolver} to be passed on to the SessionFactory: as an instance, a Class,
	 * or a String class name.
	 * 
	 * @see LocalSessionFactoryBuilder#setCurrentTenantIdentifierResolver
	 */
	public void setCurrentTenantIdentifierResolver(Object currentTenantIdentifierResolver)
	{
		this.currentTenantIdentifierResolver = currentTenantIdentifierResolver;
	}

	/**
	 * Set the Hibernate RegionFactory to use for the SessionFactory. Allows for using a Spring-managed RegionFactory instance.
	 * <p>
	 * Note: If this is set, the Hibernate settings should not define a cache provider to avoid meaningless double configuration.
	 * 
	 * @see org.hibernate.cache.spi.RegionFactory
	 * @see LocalSessionFactoryBuilder#setCacheRegionFactory
	 */
	public void setCacheRegionFactory(RegionFactory cacheRegionFactory)
	{
		this.cacheRegionFactory = cacheRegionFactory;
	}

	/**
	 * Set Hibernate properties, such as "hibernate.dialect".
	 * <p>
	 * Note: Do not specify a transaction provider here when using Spring-driven transactions. It is also advisable to omit connection
	 * provider settings and use a Spring-set DataSource instead.
	 * 
	 * @see #setDataSource
	 */
	public void setHibernateProperties(Properties hibernateProperties)
	{
		this.hibernateProperties = hibernateProperties;
	}

	/**
	 * Return the Hibernate properties, if any. Mainly available for configuration through property paths that specify individual keys.
	 */
	public Properties getHibernateProperties()
	{
		if (this.hibernateProperties == null)
		{
			this.hibernateProperties = new Properties();
		}
		return this.hibernateProperties;
	}

	/**
	 * Specify annotated entity classes to register with this Hibernate SessionFactory.
	 * 
	 * @see org.hibernate.cfg.Configuration#addAnnotatedClass(Class)
	 */
	public void setAnnotatedClasses(Class<?>... annotatedClasses)
	{
		this.annotatedClasses = annotatedClasses;
	}

	/**
	 * Specify the names of annotated packages, for which package-level annotation metadata will be read.
	 * 
	 * @see org.hibernate.cfg.Configuration#addPackage(String)
	 */
	public void setAnnotatedPackages(String... annotatedPackages)
	{
		this.annotatedPackages = annotatedPackages;
	}

	/**
	 * Specify packages to search for autodetection of your entity classes in the classpath. This is analogous to Spring's component-scan
	 * feature ({@link org.springframework.context.annotation.ClassPathBeanDefinitionScanner}).
	 */
	public void setPackagesToScan(String... packagesToScan)
	{
		this.packagesToScan = packagesToScan;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader)
	{
		this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
	}

	@Override
	public void afterPropertiesSet() throws IOException
	{
		LocalSessionFactoryBuilder sfb = new LocalSessionFactoryBuilder(this.dataSource, this.resourcePatternResolver);

		if (this.configLocations != null)
		{
			for (Resource resource : this.configLocations)
			{
				// Load Hibernate configuration from given location.
				sfb.configure(resource.getURL());
			}
		}

		if (this.mappingResources != null)
		{
			// Register given Hibernate mapping definitions, contained in resource files.
			for (String mapping : this.mappingResources)
			{
				Resource mr = new ClassPathResource(mapping.trim(), this.resourcePatternResolver.getClassLoader());
				sfb.addInputStream(mr.getInputStream());
			}
		}

		if (this.mappingLocations != null)
		{
			// Register given Hibernate mapping definitions, contained in resource files.
			for (Resource resource : this.mappingLocations)
			{
				sfb.addInputStream(resource.getInputStream());
			}
		}

		if (this.cacheableMappingLocations != null)
		{
			// Register given cacheable Hibernate mapping definitions, read from the file system.
			for (Resource resource : this.cacheableMappingLocations)
			{
				sfb.addCacheableFile(resource.getFile());
			}
		}

		if (this.mappingJarLocations != null)
		{
			// Register given Hibernate mapping definitions, contained in jar files.
			for (Resource resource : this.mappingJarLocations)
			{
				sfb.addJar(resource.getFile());
			}
		}

		if (this.mappingDirectoryLocations != null)
		{
			// Register all Hibernate mapping definitions in the given directories.
			for (Resource resource : this.mappingDirectoryLocations)
			{
				File file = resource.getFile();
				if (!file.isDirectory())
				{
					throw new IllegalArgumentException("Mapping directory location [" + resource + "] does not denote a directory");
				}
				sfb.addDirectory(file);
			}
		}

		if (this.entityInterceptor != null)
		{
			sfb.setInterceptor(this.entityInterceptor);
		}

		if (this.namingStrategy != null)
		{
			sfb.setNamingStrategy(this.namingStrategy);
		}

		if (this.jtaTransactionManager != null)
		{
			sfb.setJtaTransactionManager(this.jtaTransactionManager);
		}

		if (this.multiTenantConnectionProvider != null)
		{
			sfb.setMultiTenantConnectionProvider(this.multiTenantConnectionProvider);
		}

		if (this.currentTenantIdentifierResolver != null)
		{
			sfb.setCurrentTenantIdentifierResolver(this.currentTenantIdentifierResolver);
		}

		if (this.cacheRegionFactory != null)
		{
			sfb.setCacheRegionFactory(this.cacheRegionFactory);
		}

		if (this.hibernateProperties != null)
		{
			sfb.addProperties(this.hibernateProperties);
		}

		if (this.annotatedClasses != null)
		{
			sfb.addAnnotatedClasses(this.annotatedClasses);
		}

		if (this.annotatedPackages != null)
		{
			sfb.addPackages(this.annotatedPackages);
		}

		if (this.packagesToScan != null)
		{
			sfb.scanPackages(this.packagesToScan);
		}

		// Build SessionFactory instance.
		this.configuration = sfb;
		this.sessionFactory = buildSessionFactory(sfb);
	}

	/**
	 * Subclasses can override this method to perform custom initialization of the SessionFactory instance, creating it via the given
	 * Configuration object that got prepared by this LocalSessionFactoryBean.
	 * <p>
	 * The default implementation invokes LocalSessionFactoryBuilder's buildSessionFactory. A custom implementation could prepare the
	 * instance in a specific way (e.g. applying a custom ServiceRegistry) or use a custom SessionFactoryImpl subclass.
	 * 
	 * @param sfb
	 *            LocalSessionFactoryBuilder prepared by this LocalSessionFactoryBean
	 * @return the SessionFactory instance
	 * @see LocalSessionFactoryBuilder#buildSessionFactory
	 */
	protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder sfb)
	{
		return sfb.buildSessionFactory();
	}

	/**
	 * Return the Hibernate Configuration object used to build the SessionFactory. Allows for access to configuration metadata stored there
	 * (rarely needed).
	 * 
	 * @throws IllegalStateException
	 *             if the Configuration object has not been initialized yet
	 */
	public final Configuration getConfiguration()
	{
		if (this.configuration == null)
		{
			throw new IllegalStateException("Configuration not initialized yet");
		}
		return this.configuration;
	}

	@Override
	public SessionFactory getObject()
	{
		return this.sessionFactory;
	}

	@Override
	public Class<?> getObjectType()
	{
		return (this.sessionFactory != null ? this.sessionFactory.getClass() : SessionFactory.class);
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

	@Override
	public void destroy()
	{
		this.sessionFactory.close();
	}

}
