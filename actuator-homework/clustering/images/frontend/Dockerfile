#docker build -t vaomaohao/library-frontend:new .
#docker push vaomaohao/library-frontend:new
#docker run -it -p 3000:3000 vaomaohao/library-frontend:new
FROM node:12-alpine
WORKDIR /web
COPY package.json /web/package.json
COPY src/ /web/src/
COPY public/ /web/public/
RUN npm install
CMD ["npm", "run", "start", "--scripts-prepend-node-path=auto"]