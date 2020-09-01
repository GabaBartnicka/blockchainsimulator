FROM node:13.12.0-alpine

#ENV NPM_CONFIG_LOGLEVEL warn

# Install and configure `serve`.
RUN yarn global add serve
CMD serve -s build -l 4000
EXPOSE 4000
#EXPOSE 5000

# Install all dependencies of the current project.
COPY package.json package.json
RUN yarn

# Copy all local files into the image.
COPY . .

# Build for production.
RUN yarn config set proxy http://0.0.0.0:8080
#CMD ["yarn", "start"]
#RUN yarn start
RUN yarn run build --production


