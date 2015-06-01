#RailRoad
#Руководство разработчика

* [Описание проекта](#descriprion)
* [Используемые технологии](#technologies)
* [Основные классы проекта](#main_classes)
    * [Классы предметной области](#domain)
    * [Разбор входного файла](#parse)
    * [Класс для отрисовки](#draw)
    * [Классы для создания анимации](#animate)
    * [Проверка на столкновения](#collision)
    * [Запуск проекта](#run)
* [Сборка проекта](#build)
* [Автор](#author)
    
<a name="descriprion"></a>
#Описание проекта

Целью данного проекта является симуляция железной дороги. Поезда бывают 2 типов: пассажирские и грузовые.
Поезда первого типа следуют строго заданному маршруту, в то время как поезда второго типа генерируются случайным
образом и их маршрут так же случаен.

Одновременно в одном городе может находится не более одного пассажирского поезда. Если это условие не выполняется,
т.е. два или более поездов встречаются в одном городе, то симуляция для них заканчивается.

<a name="technologies"></a>
#Используемые технологии
* **Java 8** (**JavaFX** - встроенная графическая библиотека)
* **Maven** - система сборки java-проектов

<a name="main_classes"></a>
#Основные классы проекта
<a name="domain"></a>
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

В данном классе добавляется два поля:
* **name** - имя поезда
* **route** - маршрут поезда

Помимо базового конструктора добавился конструктор, принимающий два новых поля.
Маршрут поезда задается в виде списка [a, b, c, d], это значит, что поезд будет следовать по маршруту a -> b -> c -> d -> a, т.е. маршрут зацикленный.

Для представления городов так же был создан класс, который называется **City**:

```java
public class City {
    private int x;
    private int y;
    private String name;
    private List<City> neighbours;

    public City(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public List<City> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<City> neighbours) {
        this.neighbours = neighbours;
    }

    @Override
    public String toString() {
        return "City{" +
                "x=" + x +
                ", y=" + y +
                ", name='" + name + '\'' +
                ", neighbours=" + neighbours.stream().map(City::getName).collect(Collectors.toList()) +
                '}';
    }
}
```

В данном классе имеются следующие поля:
* **x, y** - координаты города на плоскости
* **name** - имя города
* **neighbours** - список соседних для данного города городов

#Пример использования
```java
double speed = 50.0;
String name = "train name";
City saratov = new City("Saratov", 100, 100);
City samara = new City("Samara", 200, 200);
City volgograd = new City("Volgograd", 250, 400);

saratov.setNeighbours(Arrays.asList(samara, volgogrdad));
samara.setNeighbours(Arrays.asList(saratov, volgograd));
volgograd.setNeighbours(Arrays.asList(saratov, samara));

List<City> route = Arrays.asList(saratov, samara, volgograd);
Train passengerTrain = new PassengerTrain(speed, name, route);
Train freightTrain = new FreightTrain(speed);
```

В данном примере создается следующий список городов: Саратов (координаты 100, 100, соседи: Самара, Волгоград), Самара (200, 200, соседи: Саратов, Волгоград), Волгоград (250, 400, соседи: Саратов, Самара). С помощью этого списка создается маршрут route Саратов -> Самара -> -Волгоград -> Саратов, далее создается пассажирский поезд **passengerTrain**, который имеет имя "train name" и скорость 50км/ч, а так же грузовой поезд, который имеет скорость 50км/ч.

***!!!ВАЖНО!!!***

Если в списке соседей города A есть город B, то в списке соседей города B **ОБЯЗАТЕЛЬНО** должен быть город A.

<a name="parse"></a>
#Разбор входного файла
Для разбора входного файла используется класс **Parser**. Основным методом в этом классе является метод **parse**, который разбирает входной файл, заполняя необходимые поля, к которым есть доступ с помощью геттеров.

Во входном файле могут встречаться следующие инструкции:
* **size w h** - размер окна в пикселях в ширину и высоту
* **city name x y** - город, его имя и координаты на плоскости
* **neighbours a [b, c... z]** - список соседних городов для города **a**
* **train passenger name speed route** - пассажирский поезд, задаваемый своим именем, скоростью и маршрутом
* **train freight speed** - грузовой поезд, задаваемый своей скоростью

<a name="draw"></a>
#Класс для отрисовки

За отрисовку отвечает класс **Drawer**. У него есть следующие методы:
* **drawCity** - рисует город в виде окружности в координатах, содержащихся в переданном объекте
* **drawRailRoad** - рисует железную дорогу в виде отрезка между переданным городом и его соседями

<a name="animate"></a>
#Классы для создания анимации
Для создания анимации используется два класса: **Timelines** и **Transitions**.
Класс **Transitions** содержит следующие методы:
* **getPath** - возвращает путь в виде совокупности прямых, в зависимости от типа переданного поезда
* **getPassengerPath** - возвращает путь для пассажирского поезда
* **calculateTime** - считает время, необходимое поезду для передвижения из одного города в другой
* **generateRandomRoute** - возвращает случайный путь длины n, используется только для грузовых поездов
* **createFreightPath** - возвращает путь для грузового поезда

Класс **Timelines** используется для создания с какой-то перидоичностью грузовых поездов и запуска для них анимации. Для этого используется объект **createFreightTrains**, который случайным образом создает грузовой поезд и запускает для него анимацию.

<a name="collision"></a>
#Проверка на столкновения
Так же в классе **Timelines** есть объект, который с какой-то периодичностью проверяет, не столкнулись ли несколько поездов в одном городе. Этот объект называется **checkCollisions**. При вызове метода **play** у данного объекта запустится алгоритм, который и будет выполнять всю работу.

<a name="run"></a>
#Запуск проекта
Для запуска проекта используется основной класс - **Main**, в нем запускается метод для разбора входного файла, создаются все формы, отрисовываются города и железные дороги, и, в конце концов, запускаются анимации для пассажирских и грузовых поездов, а так же алгоритм проверки коллизий. 

<a name="build"></a>
#Сборка проекта
Для сборки проекта используется **Maven** - система сборки java-проектов. Основная конфигурация находится в файле **pom.xml**. В секции **plugins** указано два плагина - один для компиляции проекта, второй - для сборки исполняемого файла.

Для запуска процесса сборки необходимо выполнить следующую команду:
```bash
mvn assembly:assembly
```

После этого в директории target создастся исполняемый файл с раширением .jar. Для его запуска нужно выполнить следующую команду:

```bash
java -jar RailRoad-1.0-jar-with-dependencies.jar input.txt
```

где input.txt - входной файл.

<a name="author"></a>
#Автор
Аветисян Севак, 251 группа, КНиИТ, 2015г.
