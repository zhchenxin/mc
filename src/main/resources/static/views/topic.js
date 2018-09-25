Vue.component('topic', {
  template: `
    <div>
      <Spin size="large" fix v-if="loading"></Spin>
      <Row>
        <Breadcrumb>
          <BreadcrumbItem to="/">Home</BreadcrumbItem>
          <BreadcrumbItem>Topic</BreadcrumbItem>
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
            <Button type="primary" @click="search">搜索</Button>
          </FormItem>
        </Form>
      </Row>
      <br>
      <Row>
        <Button type="info"  icon="refresh" @click="refresh"></Button>
        <Button type="primary" icon="plus-round" @click="addModel_show = true">添加</Button>
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

      <Modal v-model="addModel_show" :footer-hide="true" title="添加">
        <div>
          <Form ref="addModel_formData" :model="addModel_formData" :rules="addModel_ruleValidate" :label-width="80">
            <FormItem label="名称" prop="name">
              <Input v-model="addModel_formData.name" placeholder="请输入名称"/>
            </FormItem>
            <FormItem label="Topic 描述" prop="description">
              <Input v-model="addModel_formData.description" placeholder="请输入 Topic 描述"/>
            </FormItem>
            <FormItem>
              <Button type="success" :loading="addModel_doing" @click="add('addModel_formData')">提交</Button>
            </FormItem>
          </Form>
        </div>
      </Modal>

      <Modal v-model="updateModel_show" :footer-hide="true" title="更新">
        <div>
          <Form ref="updateModel_formData" :model="updateModel_formData" :rules="updateModel_ruleValidate" :label-width="80">
            <FormItem label="名称" prop="name">
              <Input v-model="updateModel_formData.name" placeholder="请输入名称"/>
            </FormItem>
            <FormItem label="Topic 描述" prop="description">
              <Input v-model="updateModel_formData.description" placeholder="请输入 Topic 描述"/>
            </FormItem>
            <FormItem>
              <Button type="success" :loading="updateModel_doing" @click="update('updateModel_formData')">提交</Button>
            </FormItem>
          </Form>
        </div>
      </Modal>

    </div>
  `,
  data: function () {
    return {
      // 表格加载loadding
      loading: false,

      // 表格内容
      tableColumns: [
        {type: 'selection', width: 60, align: 'center'},
        {title: 'id', key: 'id', width: 60, align: 'right'},
        {title: '名称', key: 'name'},
        {title: '描述', key: 'description'},
        {title: '创建时间', key: 'createDate'},
        {
          title: 'Action',
          key: 'action',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    var topic = this.tableData[params.index]
                    this.updateModel_formData.id = topic.id
                    this.updateModel_formData.name = topic.name
                    this.updateModel_formData.description = topic.description
                    this.updateModel_show=true
                  }
                }
              }, '编辑'),
              h('Button', {
                props: {
                  type: 'error',
                  size: 'small'
                },
                on: {
                  click: () => {
                    var topic = this.tableData[params.index]
                    this.delete(topic.id)
                  }
                }
              }, '删除')
            ]);
          }
        }
      ],

      // 表格内容
      tableData: [],

      // 分页数据
      totalCount: 0,
      currentPage: 1,
      limit: 10,

      // 搜索栏
      formSearch: {
        topicId: ''
      },
      topic_list: [],

      // 添加模态框表单数据
      addModel_show: false,
      addModel_doing: false,
      addModel_formData: {
        name: '',
        description: '',
      },
      addModel_ruleValidate: {
        name: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
        ],
        description: [
          {required: true, message: 'Topic 描述不能为空', trigger: 'blur'}
        ]
      },   
      
      // 更新模态框表单数据
      updateModel_show: false,
      updateModel_doing: false,
      updateModel_formData: {
        id: 0,
        name: '',
        description: '',
      },
      updateModel_ruleValidate: {
        name: [
          {required: true, message: '名称不能为空', trigger: 'blur'}
        ],
        description: [
          {required: true, message: 'Topic 描述不能为空', trigger: 'blur'}
        ]
      },
    }
  },
  mounted: function () {
    this.formSearch.topic_id = this.$route.query.topicId
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
      this.currentPage = 1
      this.featchTableData()
      this.$router.replace({ path: '/topic', query: { topicId: this.formSearch.topicId }})
    },
    featchTableData: function() {
      this.loading = true
      client.get('/topic', {params:{page: this.currentPage, limit: this.limit, topicId: this.formSearch.topicId}}).then((response) => {
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
    add: function(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.addModel_doing = true
          client.post('/topic/create', this.addModel_formData).then(() => {
            this.addModel_show = false
            this.addModel_doing = false
            this.featchTableData()
          }).catch((error) => {
            this.addModel_doing = false
            this.$Message.error({title: error})
          })
        }
      })
    },
    update: function(name) {
      this.$refs[name].validate((valid) => {
        if (valid) {
          this.updateModel_doing = true
          client.post('/topic/update/' + this.updateModel_formData.id, this.updateModel_formData).then(() => {
            this.updateModel_show = false
            this.updateModel_doing = false
            this.featchTableData()
          }).catch((error) => {
            this.updateModel_doing = false
            this.$Message.error({title: error})
          })
        }
      })
    },
    delete: function(id) {
      client.post('/topic/delete/' + id, null).then(() => {
        this.featchTableData()
      }).catch((error) => {
        this.$Message.error({title: error})
      })
    },
  },
})
