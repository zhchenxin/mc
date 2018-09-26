Vue.component('user-menu', {
  template: `
    <div>
      <router-link to="/"><MenuItem name="/"><Icon type="home"></Icon><span>Dashboard</span></MenuItem></router-link>
      <router-link to="/topic"><MenuItem name="/topic"><Icon type="help-buoy"></Icon><span>Topic</span></MenuItem></router-link>
      <router-link to="/customer"><MenuItem name="/customer"><Icon type="hammer"></Icon><span>消费者</span></MenuItem></router-link>
      <router-link to="/cron"><MenuItem name="/cron"><Icon type="hammer"></Icon><span>定时任务</span></MenuItem></router-link>
      <router-link to="/message"><MenuItem name="/message"><Icon type="ios-paper"></Icon><span>消息日志</span></MenuItem></router-link>
      <router-link to="/failed_message"><MenuItem name="/failed_message"><Icon type="ios-paper"></Icon><span>异常消息</span></MenuItem></router-link>
    </div>
  `
})

const router = new VueRouter({
  mode: 'hash',
  routes: [
    {path: '/', component: {template: '<dashboard />'}},
    {path: '/topic', component: {template: '<topic />'}},
    {path: '/customer', component: {template: '<customer/>'}},
    {path: '/cron', component: {template: '<cron/>'}},
    {path: '/message', component: {template: '<message />'}},
    {path: '/failed_message', component: {template: '<failed_message />'}},
  ]
})

new Vue({
  router,
  el: '#app',
  data: {
    active_menu: '/'
  },
  template: `
    <div class="layout">
      <Sider :style="{position: 'fixed', height: '100vh', left: 0, overflow: 'auto'}">
        <Menu :active-name="active_menu" theme="dark" width="auto" :open-names="['1']">
          <user-menu/>
        </Menu>
      </Sider>
      <Layout :style="{marginLeft: '200px'}">
        <Content :style="{padding: '16px 16px 16px'}">
          <Card>
            <router-view/>
          </Card>
        </Content>
      </Layout>
    </div>
  `,
  created: function () {
    this.fetchData()
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'fetchData'
  },
  methods: {
    fetchData: function() {
      this.active_menu = router.currentRoute.path
    }
  }
})