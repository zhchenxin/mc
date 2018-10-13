const router = new VueRouter({
  mode: 'hash',
  routes: [
    {path: '/', component: {template: '<dashboard />'}},
    {path: '/topic', component: {template: '<topic />'}},
    {path: '/customer', component: {template: '<customer/>'}},
    {path: '/cron', component: {template: '<cron/>'}},
    {path: '/message_log', component: {template: '<message_log />'}},
    {path: '/failed_message', component: {template: '<failed_message />'}},
  ]
})

new Vue({
  router,
  el: '#app',
  data: {
  },
  template: `
    <el-container>
      <el-aside width="200px">
        <el-menu route class="el-menu-vertical-demo" :default-active="$route.path" @select="handleSelect">
          <el-menu-item index="/">Dashboard</el-menu-item>
          <el-menu-item index="/topic">Topic</el-menu-item>
          <el-menu-item index="/customer">消费者</el-menu-item>
          <el-menu-item index="/cron">定时任务</el-menu-item>
          <el-menu-item index="/message_log">消息日志</el-menu-item>
          <el-menu-item index="/failed_message">异常消息</el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-main>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
  `,
  methods: {
    handleSelect: function(index) {
      router.push(index)
    },
  }
})