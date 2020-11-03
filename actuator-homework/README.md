## Домашние задания 16,17 Spring Actuator, Docker

Был добавлен actuator, включены metrics, logfile, health, prometheus. Реализован кастомный  
HealthIndicator, интегрированный с Micrometer.  
Бэкенд и prometheus деплойменты разворачивались на кластере kubernetes (minikube).  
Контейнер с фронтендом разворачивался в контейнере на localhost-е.  
  
Скриншот некоторых графиков prometheus:  
Демонстрация работы HealthIndicator:  
![health](clustering/static/health.png)
Демонстрация отслеживания метрики расходуемого CPU backend приложением:  
![cpu](clustering/static/cpu_usage.png)  

### для справки, в mongobee есть баг при работе с Mongo Atlas, для устранения ссылка:  
https://stackoverflow.com/questions/49974594/reading-of-dbname-system-indexes-failed-on-atlas-cluster-by-mongobee-after-getti/49977135#49977135