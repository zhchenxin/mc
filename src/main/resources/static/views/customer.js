Vue.component('customer', {
  template: `
    <div>
      <Spin size="large" fix v-if="loading"></Spin>
      <Row>
        <Breadcrumb>
          <BreadcrumbItem to="/">Home</BreadcrumbItem>
          <BreadcrumbItem>Customer</BreadcrumbItem>
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
          <FormItem>
            <Button type="primary" @click="search()">搜索</Button>
          </FormItem>
        </Form>
      </Row>
      <br>
      <Row>
        <Button type="info"  icon="refresh" @click="refresh"></Button>
        <router-link to="/customer/add"><Button type="primary"  icon="plus-round">添加</Button></router-link>
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
        {type: 'selection', width: 60, align: 'center'},
        {title: 'id', key: 'id', width: 60, align: 'right'},
        {title: '名称', key: 'name'},
        {title: 'topic', key: 'topicName'},
        {title: 'api', key: 'api'},
        {title: '超时时间', key: 'timeout'},
        {title: '重试次数', key: 'attempts'},
        {title: '创建时间', key: 'createDate'},
      ],
      tableData: [],
      totalCount: 0,
      currentPage: 1,
      limit: 10,
      formSearch: {
        topicId: ''
      },
      topic_list: [],
    }
  },
  mounted: function () {
    this.formSearch.topic_id = this.$route.query.topic_id
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
    refresh: function () {
      this.featchTableData()
    },
    search: function() {
      this.featchTableData()
      this.$router.replace({ path: '/customer', query: { topicId: this.formSearch.topicId }})
    },
    featchTableData: function () {
      this.loading = true
      client.get('/customer', {
        params: {
          page: this.currentPage,
          limit: this.limit,
          topicId: this.formSearch.topicId
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


Vue.component('customer-add', {
  template: `
    <div>
      <Row>
        <Breadcrumb>
          <BreadcrumbItem to="/">Home</BreadcrumbItem>
          <BreadcrumbItem to="/topic">Topic</BreadcrumbItem>
          <BreadcrumbItem>添加 Topic</BreadcrumbItem>
        </Breadcrumb>
      </Row>
      <br>
      <Row>
        <Form ref="formValidate" :model="formValidate" :rules="ruleValidate" :label-width="80">
          <FormItem label="名称" prop="name">
            <Input v-model="formValidate.name" placeholder="请输入名称"/>
          </FormItem>
          <FormItem label="Topic" prop="topic_id">
            <Select v-model="formValidate.topic_id">
                <Option v-for="item in topic_list" :value="item.id" :key="item.id">{{ item.name }}</Option>
            </Select>
          </FormItem>
          <FormItem label="api" prop="api">
            <Input v-model="formValidate.api"/>
          </FormItem>
          <FormItem label="超时时间" prop="timeout">
            <Input v-model="formValidate.timeout"/>
          </FormItem>
          <FormItem label="重试次数" prop="attempts">
            <Input v-model="formValidate.attempts"/>
          </FormItem>
          <FormItem>
            <Button type="success" @click="handleSubmit('formValidate')">提交</Button>
          </FormItem>
        </Form>
      </Row>
    </div>
  `,
  data: function () {
    return {
      topic_list: [],
      formValidate: {
        name: '',
        topic_id: '',
        api: '',
        timeout: '',
        attempts: '',
      },
      ruleValidate: {
        name: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
        ],
        topic_id: [
          {required: true, type: 'number', message: '', trigger: 'change'}
        ],
        api: [
          {required: true, message: 'api不能为空', trigger: 'blur'}
        ],
        timeout: [
          {required: true, message: '超时时间不能为空', trigger: 'blur'}
        ],
        attempts: [
          {required: true, message: '重试次数不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  created: function () {
    client.get('/topic', {params: {limit: 10000}}).then((response) => {
      this.topic_list = response.data.data.list
    }).catch((error) => {
      this.$Message.error(error)
      this.topic_list = []
    })
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          client.post('/customer', this.formValidate).then(() => {
            this.$router.go('-1')
          }).catch((error) => {
            this.$Message.error(error)
          });
        } else {
          this.$Message.error('参数校验错误')
        }
      })
    }
  }
});
