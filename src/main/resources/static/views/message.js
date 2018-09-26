Vue.component('message', {
  template: `
    <div>
      <Spin size="large" fix v-if="loading"></Spin>
      <Row>
        <Breadcrumb>
          <BreadcrumbItem to="/">Home</BreadcrumbItem>
          <BreadcrumbItem>消息日志</BreadcrumbItem>
        </Breadcrumb>
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
        {title: '消费者名称', key: 'customerName'},
        {title: 'topic名称', key: 'topicName'},
        {title: '请求', key: 'request'},
        // {title: '响应', key: 'response'},
        {title: '错误信息', key: 'error'},
        {title: '重试次数', key: 'attempts'},
        {title: '创建时间', key: 'createDate'},
      ],
      tableData: [],
      totalCount: 0,
      currentPage: 1,
      limit: 10,
    }
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
    featchTableData: function() {
      this.loading = true
      client.get('/message/log', {
        params:{
          page: this.currentPage,
          limit: this.limit,
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