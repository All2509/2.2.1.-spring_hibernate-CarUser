package hiber.config;

import hiber.model.Car;
import hiber.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;


@Configuration  //класс содержит определения бинов для контекста Spring.
@PropertySource("classpath:db.properties")  //Spring должен загружать свойства из файла db.properties
@EnableTransactionManagement //Включает поддержку управления транзакциями в Spring. Это позволяет
        // использовать аннотации, такие как @Transactional, для управления транзакциями в сервисах.
@ComponentScan(value = "hiber") //Указывает Spring сканировать пакет hiber на наличие компонентов
// (например, сервисов и DAO), которые будут автоматически зарегистрированы как
// бины в контексте приложения.


public class AppConfig {

   @Autowired  //env будет содержать доступ к свойствам, загруженным из файла db.properties.
   private Environment env;  //interface Environment extends PropertyResolver , а у последнего
   //есть метод getProperty, который используется для получения значения свойства по заданному ключу

   @Bean
   public DataSource getDataSource() {   //DataSourse это интерфейс
      DriverManagerDataSource dataSource = new DriverManagerDataSource(); //DriverManagerDataSource —
      // это класс из библиотеки Spring, который реализует интерфейс DataSource. Он предоставляет
      // простой способ для получения соединений с базой данных, используя стандартный механизм JDBC
      dataSource.setDriverClassName(env.getProperty("db.driver")); //setDriverClassName это метод DriverManagerDataSource
      dataSource.setUrl(env.getProperty("db.url")); //setUrl метод class AbstractDriverBasedDataSource extends
      // AbstractDataSource, который имплементирует DataSourse
      dataSource.setUsername(env.getProperty("db.username"));
      dataSource.setPassword(env.getProperty("db.password"));
      return dataSource; //настроенный объект dataSource, который теперь содержит все необходимые
      // параметры для подключения к базе данных
   }


   //LocalSessionFactoryBean — это класс из библиотеки Spring, который предоставляет конфигурацию для Hibernate.
   // Он создает SessionFactory, который используется для получения сессий Hibernate.
   @Bean
   public LocalSessionFactoryBean getSessionFactory() { //LocalSessionFactoryBean Этот класс
      // является частью Spring и используется для настройки Hibernate SessionFactory.
      LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
      factoryBean.setDataSource(getDataSource());// Этот метод устанавливает DataSource,
      // который будет использоваться для подключения к базе данных. В данном случае, он вызывает
      // метод getDataSource(), который мы рассматривали ранее, чтобы получить настроенный объект DataSource.

      Properties props = new Properties();  //Properties является подклассом класса Hashtable, что означает,
      // что он наследует все методы Hashtable .В Properties ключи и значения хранятся в виде строк
      props.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
      //Этот код устанавливает свойство hibernate.show_sql, которое определяет, будет ли Hibernate выводить SQL-запросы
      // в консоль. Значение берется из файла свойств с помощью env.getProperty("hibernate.show_sql").
      props.put("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
      // Этот код устанавливает свойство hibernate.hbm2ddl.auto, которое управляет автоматическим созданием и
      // обновлением схемы базы данных. Значение также берется из файла свойств.

      factoryBean.setHibernateProperties(props);
      factoryBean.setAnnotatedClasses(User.class, Car.class);
      return factoryBean;
   }

   @Bean
   public HibernateTransactionManager getTransactionManager() {
      HibernateTransactionManager transactionManager = new HibernateTransactionManager();
      transactionManager.setSessionFactory(getSessionFactory().getObject());
      return transactionManager;
   }
}