### Предварительно был поднят minikube установка: https://phoenixnap.com/kb/install-minikube-on-ubuntu  
- Создание namespace: kubectl create namespace sandbox
- Задание квоты: kubectl apply -f quota.yaml --namespace=sandbox
- Применяем темплейт spring приложения: kubectl apply -f backend-template.yaml --namespace=sandbox  
- Применяем темплейт prometheus деплоймента: kubectl apply -f prometheus-template.yaml --namespace=sandbox
- Задаём bind ip -> dns name: добавляем в /etc/hosts строку: <ipV4...>  app.library.backend.otus