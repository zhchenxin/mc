Vue.component('message', {
  template: `
    <div>
      <Spin size="large" fix v-if="loading"></Spin>
      <Row>
        <Breadcrumb>
          <BreadcrumbItem to="/">Home</BreadcrumbItem>
          <BreadcrumbItem>Message</BreadcrumbItem>
        </Breadcrumb>
      </Row>
      <br>
      <Row>
        <Form label-position="left" :label-width="80" inline>
          <FormItem label="Topic" prop="topicId">
            <Select v-model="formSearch.topicId">
                <Option v-for="item in topic_list" :value="item.id" :key="item.id">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem label="Customer" prop="customer_id">
            <Select v-model="formSearch.customerId">
                <Option v-for="item in customer_list" :value="item.id" :key="item.id">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem label="状态" >
            <Select v-model="formSearch.status">
              <Option v-for="item in statusList" :value="item.id" :key="item.id">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem>
            <Button type="primary" @click="search()">搜索</Button>
          </FormItem>
        </Form>
      </Row>
      <br>
      <Row>
        <Table :border="false" :stripe="true" :show-header="true" :data="tableData" :columns="tableColumns"></Table>
        <div style="margin: 10px;overflow: hidden">
          <div style="float: right;">
            <Page :total="totalCount" :current="currentPage" show-sizer size="small" show-total @on-change="changePage" @on-page-size-change="changePageSize"></Page>
          </div>
        </div>
      </Row>
    </div>
  `,
  data: function () {
    return {
      loading: false,
      tableColumns: [
        {title: 'id', key: 'id', width: 60, align: 'right'},
        {title: 'topic', key: 'topicName'},
        {title: 'customer', key: 'customerName'},
        {title: '消息内容', key: 'message'},
        {title: '重试次数', key: 'attempts'},
        {title: '状态', key: 'status'},
        {title: '开始执行时间', key: 'availableDate'},
        {title: '创建时间', key: 'createDate'},
      ],
      tableData: [],
      totalCount: 0,
      currentPage: 1,
      limit: 10,
      formSearch: {
        topicId: '',
        customerId: '',
        status: 0,
      },
      topic_list: [],
      customer_list: [],
      statusList: [
        {id: 0, name: '全部'},
        {id: 1, name: '等待中'},
        {id: 2, name: '执行中'},
        {id: 3, name: '成功'},
        {id: 4, name: '失败'},
      ]
    }
  },
  mounted: function() {
    this.formSearch.topicId = this.$route.query.topicId
    this.formSearch.customerId = this.$route.query.customerId
    this.formSearch.status = this.$route.query.status
    this.featchTableData()
    client.get('/topic', {params: {limit: 10000}}).then((response) => {
      this.topic_list = response.data.data.list
    }).catch((error) => {
      this.$Message.error(error)
      this.topic_list = []
    })
  },
  methods: {
    changePage: function (page) {
      this.currentPage = page
      this.featchTableData()
    },
    changePageSize: function (size) {
      this.limit = size
      this.featchTableData()
    },
    refresh: function() {
      this.featchTableData()
    },
    search: function() {
      this.featchTableData()
      this.$router.replace({
        path: '/message',
        query: {
          topicId: this.formSearch.topicId,
          customerId: this.formSearch.customerId,
          status: this.formSearch.status
      }})
    },
    featchTableData: function() {
      this.loading = true
      client.get('/message', {
        params:{
          page: this.currentPage,
          limit: this.limit,
          topicId: this.formSearch.topicId,
          customerId: this.formSearch.customerId,
          status: this.formSearch.status
        }
      }).then((response) => {
        this.tableData = response.data.data.list
        this.totalCount = response.data.data.mate.total
        this.currentPage = response.data.data.mate.currentPage
        this.loading = false
      }).catch((error) => {
        this.$Message.error(error)
        this.tableData = []
        this.loading = false
      })
    }
  },
})