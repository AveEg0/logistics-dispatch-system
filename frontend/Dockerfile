FROM node:20
LABEL authors="karma"
WORKDIR /app

COPY package.json .

RUN npm install

COPY . .

CMD ["npm", "run", "dev"]