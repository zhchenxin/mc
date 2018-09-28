Vue.component('message_log', {
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
        <Form label-position="left" :label-width="80" inline>
          <FormItem label="消费者" prop="customerId">
            <Select v-model="Search_data.customerId" filterable >
                <Option v-for="item in Search_customerList" :value="item.id" :key="item.id">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem>
            <Button type="primary" @click="search">搜索</Button>
          </FormItem>
        </Form>
      </Row>
      <br>
      <Row>
        <Button type="info"  icon="refresh" @click="refresh"></Button>
      </Row>
      <br>
      <Row>
        <Table :border="false" :stripe="true" :show-header="true" :data="tableData" :columns="tableColumns"></Table>
        <div style="margin: 10px;overflow: hidden">
          <div style="float: right;">
            <Page :total="totalCount" :current="currentPage" show-sizer :page-size="25" :page-size-opts="[25, 50, 100, 250, 500]" size="small" show-total @on-change="changePage" @on-page-size-change="changePageSize"></Page>
          </div>
        </div>
      </Row>
    </div>
  `,
  data: function () {
    return {
      loading: false,

      // 列表
      tableColumns: [
        {title: 'id', key: 'id'},
        {title: '消费者名称', key: 'customerName'},
        {title: 'topic名称', key: 'topicName'},
        {title: '请求', key: 'request'},
        {title: '响应', key: 'response', type: 'expand'},
        {title: '错误信息', key: 'error'},
        {title: '创建时间', key: 'createDate'},
      ],
      tableData: [],

      // 分页
      totalCount: 0,
      currentPage: 1,
      limit: 25,

      // 搜索栏
      Search_data: {
        customerId: ''
      },
      Search_customerList: [],
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
    search: function() {
      this.featchTableData()
    },
    featchTableData: function() {
      this.loading = true
      console.log(this.limit)
      client.get('/message/log', {
        params:{
          page: this.currentPage,
          limit: this.limit,
          customerId: this.Search_data.customerId
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