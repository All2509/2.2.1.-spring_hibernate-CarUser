package hiber;

import hiber.config.AppConfig;
import hiber.model.User;
import hiber.model.Car;
import hiber.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.persistence.NoResultException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainApp {
   public static void main(String[] args) throws SQLException {
      AnnotationConfigApplicationContext context =
              new AnnotationConfigApplicationContext(AppConfig.class);

      UserService userService = context.getBean(UserService.class);

      // Создаем список пользователей
      List<User> users = new ArrayList<>();
      users.add(new User("Vasya", "Vasechkin", "vasechkin@mail.io"));
      users.add(new User("Petya", "Sidorov", "sidorov@mail.io"));
      users.add(new User("Olga", "Petrova", "petrova@mail.io"));
      users.add(new User("Svetlana", "Ivanova", "ivanova@mail.io"));

      // Сохраняем пользователей в БД
      for (User user : users) {
         userService.add(user);
      }

      // Создаем список машин
      List<Car> cars = new ArrayList<>();
      cars.add(new Car("Volvo", 9));
      cars.add(new Car("BMW", 325));
      cars.add(new Car("Suzuki", 4));
      cars.add(new Car("Lada", 21014));

      // Сохраняем машины в БД
      for (Car car : cars) {
         userService.addCar(car);
      }

      // Получаем список пользователей и машин из БД
      List<User> savedUsers = userService.listUsers();
      List<Car> savedCars = userService.listCars();
      System.out.println(savedUsers);
      System.out.println(savedCars);
      // Раздаем пользователям машины
      for (int i = 0; i < savedUsers.size(); i++) {
         if (i < savedCars.size()) {
            savedUsers.get(i).setCar(savedCars.get(i));
         }
      }

      // Сохраняем обновленных пользователей с машинами в БД
      for (User user : savedUsers) {
         userService.update(user);
      }

      // Пользователи с машинами
      for (User user : userService.listUsers()) {
         System.out.println(user + " " + user.getCar());
      }

      // Достать юзера, владеющего машиной по ее модели и серии
      System.out.println(userService.getUserByCar("BMW", 325));

      // Нет такого юзера с такой машиной
      try {
         User notFoundUser = userService.getUserByCar("GAZ", 4211);
      } catch (NoResultException e) {
         System.out.println("User not found");
      }

      context.close();
   }
}