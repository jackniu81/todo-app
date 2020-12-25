import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import ElementPlus from 'element-plus';
import 'element-plus/lib/theme-chalk/index.css';

import axios from 'axios'
import VueAxios from 'vue-axios'

createApp(App)
  .use(ElementPlus)
  .use(router)
  .use(VueAxios, axios)
  .mount("#app");
