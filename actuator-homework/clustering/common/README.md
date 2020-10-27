### Предварительно был поднят minikube установка: https://phoenixnap.com/kb/install-minikube-on-ubuntu  
- Создание namespace: kubectl create namespace sandbox
- Задание квоты: kubectl apply -f quota.yaml --namespace=sandbox
- Создаём ingress: kubectl apply -f ingress.yaml --namespace=sandbox
- Задаём bind ip -> dns name: добавляем в /etc/hosts строку: <ipV4...>  <hostname>