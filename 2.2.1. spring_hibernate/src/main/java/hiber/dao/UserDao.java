package hiber.dao;

import hiber.model.User;
import hiber.model.Car;

import java.util.List;

public interface UserDao {
   void add(User user);

   List<User> listUsers();

   User getUserByCar(String model, int series);

   void addCar(Car car);

   List<Car> listCars();

   void update(User user);
}