#RailRoad
#Руководство разработчика

* Описание проекта
* Используемые технологии
* Основные классы проекта
    * Классы предметной области
    * Разбор входного файла
    * Класс для отрисовки
    * Класс для создания анимации
    
#Описание проекта

Целью данного проекта является симуляция железной дороги. Поезда бывают 2 типов: пассажирские и грузовые.
Поезда первого типа следуют строго заданному маршруту, в то время как поезда второго типа генерируются случайным
образом и их маршрут так же случаен.

Одновременно в одном городе может находится не более одного пассажирского поезда. Если это условие не выполняется,
т.е. два или более поездов встречаются в одном городе, то симуляция для них заканчивается.

#Используемые технологии
* **Java 8** (**JavaFX** - встроенная графическая библиотека)
* **Maven** - система сборки java-проектов

#Основные классы проекта
#Классы предметной области

Основным классом предметной области является абстрактный класс **Train**:
```java
public abstract class Train {
    private double speed;

    protected Train(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public abstract String toString();
}
```

Данный класс содержит лишь общие поля, которые будут присутствовать как в классе пассажирского поезда,
так в классе грузового. В данном случае это поле **speed**, отвечающее за скорость нашего поезда.
Мы ограничиваем пользователя единственным конструктором, который принимает на вход скорость поезда, т.к. без этой
информации создать объект класс не представляется возможным. Так же присутствуют метод для доступа к полю скорости
и метод, возврающающий текствое представление нашего объекта.

**FreightTrain** - класс грузового поезда.

```java
public class FreightTrain extends Train {
    public FreightTrain(double speed) {
        super(speed);
    }

    @Override
    public String toString() {
        return "FreightTrain {speed = " + getSpeed() + "}";
    }
}
```

Данный класс наследует наш базовый класс **Train** и переопределяет лишь метод, отвечающий за текстовое представление
объекта, т.к. другие методы в данном классе не используются.

**PassengerTrain** - класс пассажирского поезда.

```java
public class PassengerTrain extends Train {
    private String name;
    private List<City> route;

    private PassengerTrain(double speed) {
        super(speed);
    }

    public PassengerTrain(double speed, String name, List<City> route) {
        super(speed);
        this.name = name;
        this.route = route;
    }

    public String getName() {
        return name;
    }

    public List<City> getRoute() {
        return route;
    }

    @Override
    public String toString() {
        return "PassengersTrain: {name = " + name + ", route = " + route.stream().map(City::getName).collect(Collectors.toList())+ "}";
    }
}
```

