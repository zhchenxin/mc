import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'


Vue.use(VueRouter)
Vue.use(Element, { size: 'small', zIndex: 3000 });

Vue.config.productionTip = false

// router
import Cron from './components/Cron.vue'
import Customer from './components/Customer.vue'
import Topic from './components/Topic.vue'
import Dashboard from './components/Dashboard.vue'
import FailedMessage from './components/FailedMessage.vue'
import MessageLog from './components/MessageLog.vue'

const router = new VueRouter({
  mode: 'hash',
  routes: [
    {path: '/', component: Dashboard},
    {path: '/topic', component: Topic},
    {path: '/customer', component: Customer},
    {path: '/cron', component: Cron},
    {path: '/message_log', component: MessageLog},
    {path: '/failed_message', component: FailedMessage},
  ]
})

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
