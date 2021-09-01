import Vue from 'vue'
import VueRouter from 'vue-router'

import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import { DropdownPlugin, TablePlugin } from 'bootstrap-vue'

import App from './App.vue'
import MainPage from './components/MainPage.vue'
import SignUp from './components/SignUp.vue'

Vue.config.productionTip = false

Vue.use(VueRouter);
Vue.use(BootstrapVue);
Vue.use(IconsPlugin);
Vue.use(DropdownPlugin);
Vue.use(TablePlugin);

const routes = [
    { path: '/', component: MainPage},
    { path: '/sign-up', component: SignUp}
];

const router = new VueRouter({routes});

new Vue({
    el: "#app",
    router: router,
    render: h => h(App)
}).$mount('#app')
