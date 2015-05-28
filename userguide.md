#RailRoad
#Руководство пользователя

* [Необходимое программное обеспечение](#required)
* [Входной файл](#input)
* [Запуск](#run)

<a name="required"></a>
#Необходимое программное обеспечение
Для работы приложения необходима установленная **java** версии не ниже 8.0. Последнюю версию **java** можно скачать
по ***[этой ссылке](https://www.java.com/ru/)***.

<a name="input"></a>
#Входной файл
Входной файл должен содержать следующие инструкции:
* **size w h** - размер окна в пикселях в ширину и высоту
* **city name x y** - город, его имя и координаты на плоскости
* **neighbours a [b, c... z]** - список соседних городов для города **a**
* **train passenger name speed route** - пассажирский поезд, задаваемый своим именем, скоростью и маршрутом
* **train freight speed** - грузовой поезд, задаваемый своей скоростью

Пример входного файла:
```
size 410 410

city Saratov 100 200
city Voronezh 140 300
city Samara 200 200
city Moscow 200 100
city Novgorod 300 100
city Kazan 360 260

neighbours Saratov Moscow Samara Voronezh
neighbours Novgorod Moscow Voronezh Kazan
neighbours Samara Saratov Moscow Kazan
neighbours Moscow Saratov Samara Novgorod
neighbours Kazan Samara Novgorod
neighbours Voronezh Saratov Novgorod

train passenger 137 60 Moscow Samara Saratov
train passenger 009 100 Novgorod Moscow Saratov Voronezh
train passenger 003 80 Kazan Samara Moscow Novgorod

train freight 50
```

Работа приложения на этих входных данных:
![Imgur](http://i.imgur.com/541e5Wl.png?1)

<a name="run"></a>
#Запуск
Для запуска приложения используется следующая команда:

```bash
java -jar RailRoad-1.0-jar-with-dependencies.jar input.txt
```

* **RailRoad-1.0-jar-with-dependencies.jar** - исполняемый файл приложения
* **input.txt** - входной файл
