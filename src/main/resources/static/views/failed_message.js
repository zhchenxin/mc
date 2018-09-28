Vue.component('failed_message', {
  template: `
    <div>
      <Spin size="large" fix v-if="loading"></Spin>
      <Row>
        <Breadcrumb>
          <BreadcrumbItem to="/">Home</BreadcrumbItem>
          <BreadcrumbItem>异常消息</BreadcrumbItem>
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
        <Button type="primary" @click="retryAll">批量重试</Button>
        <Button type="primary" @click="deleteAll">批量删除</Button>
      </Row>
      <br>
      <Row>
        <Table :border="false" :stripe="true" :show-header="true" :data="tableData" :columns="tableColumns" @on-selection-change="selectChange"></Table>
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

      selection: [],

      // 表格数据
      tableColumns: [
        {type: 'selection'},
        {title: 'id', key: 'id'},
        {title: '消费者名称', key: 'customerName'},
        {title: 'topic名称', key: 'topicName'},
        {title: '消息内容', key: 'message'},
        {title: '错误信息', key: 'error'},
        {title: '重试次数', key: 'attempts'},
        {title: '创建时间', key: 'createDate'},
        {
          title: 'Action',
          key: 'action',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {props: {type: 'error',size: 'small'},style: {marginRight: '5px'},on: {click: () => {this.retry(params.index)}}}, '重试'),
              h('Button', {props: {type: 'error',size: 'small'},on: {click: () => {this.delete(params.index)}}}, '删除')
            ]);
          }
        }
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
    selectChange: function(selection) {
      this.selection = selection
    },
    featchTableData: function() {
      this.loading = true
      client.get('/message/failed', {
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
    },
    retry: function(index) {
      var data = this.tableData[index]
      client.post('/message/retry', {'messageId': data.id}).then(() => {
        this.featchTableData()
      }).catch((error) => {
        this.$Message.error(error)
      })
    },
    retryAll: function() {
      var requests = []
      for (var i = 0; i < this.selection.length; i++) {
        var request = client.post('/message/retry', {'messageId': this.selection[i].id})
        requests.push(request)
      }
      axios.all(requests).then(axios.spread(function () {
        this.featchTableData()
      })).catch((error) => {
        this.featchTableData()
        console.log(error)
      })
    },
    delete: function(index) {
      var data = this.tableData[index]
      client.post('/message/delete', {'messageId': data.id}).then(() => {
        this.featchTableData()
      }).catch((error) => {
        this.$Message.error(error)
      })
    },
    deleteAll: function() {
      var requests = []
      for (var i = 0; i < this.selection.length; i++) {
        var request = client.post('/message/delete', {'messageId': this.selection[i].id})
        requests.push(request)
      }
      axios.all(requests).then(axios.spread(function () {
        this.featchTableData()
      })).catch((error) => {
        this.featchTableData()
        console.log(error)
      })
    },
  },
})