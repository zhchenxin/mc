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
        <Button type="primary" icon="plus-round" @click="showAdd">添加</Button>
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

      <Modal v-model="AddForm_show" :footer-hide="true" title="添加">
        <div>
          <Form ref="AddForm_formData" :model="AddForm_formData" :label-width="80">
            <FormItem label="名称" prop="name">
              <Input v-model="AddForm_formData.name" placeholder="请输入名称"/>
            </FormItem>
            <FormItem label="Topic 描述" prop="description">
              <Input v-model="AddForm_formData.description" placeholder="请输入 Topic 描述"/>
            </FormItem>
            <FormItem>
              <Button type="success" :loading="AddForm_doing" @click="add()">提交</Button>
            </FormItem>
          </Form>
        </div>
      </Modal>

      <Modal v-model="EditForm_show" :footer-hide="true" title="更新">
        <div>
          <Form ref="EditForm_formData" :model="EditForm_formData" :label-width="80">
            <FormItem label="名称" prop="name">
              <Input v-model="EditForm_formData.name" placeholder="请输入名称"/>
            </FormItem>
            <FormItem label="Topic 描述" prop="description">
              <Input v-model="EditForm_formData.description" placeholder="请输入 Topic 描述"/>
            </FormItem>
            <FormItem>
              <Button type="success" :loading="EditForm_doing" @click="edit()">提交</Button>
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
              h('Button', {props: {type: 'primary',size: 'small'},style: {marginRight: '5px'},on: {click: () => {this.showEdit(params.index)}}}, '编辑'),
              h('Button', {props: {type: 'error',size: 'small'},on: {click: () => {this.delete(params.index)}}}, '删除')
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
      AddForm_show: false,
      AddForm_doing: false,
      AddForm_formData: {
        name: '',
        description: '',
      },
      
      // 更新模态框表单数据
      EditForm_show: false,
      EditForm_doing: false,
      EditForm_formData: {
        id: 0,
        name: '',
        description: '',
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
    showAdd: function() {
      this.AddForm_formData = {
        name: '',
        description: '',
      }
      this.AddForm_show = true
    },
    add: function() {
      this.AddForm_doing = true
      client.post('/topic/create', this.AddForm_formData).then(() => {
        this.AddForm_show = false
        this.AddForm_doing = false
        this.featchTableData()
      }).catch((error) => {
        this.AddForm_doing = false
        this.$Message.error(error)
      })
    },
    showEdit: function(index) {
      var topic = this.tableData[index]
      this.EditForm_formData.id = topic.id
      this.EditForm_formData.name = topic.name
      this.EditForm_formData.description = topic.description
      this.EditForm_show=true
    },
    edit: function() {
      this.EditForm_doing = true
      client.post('/topic/update', this.EditForm_formData).then(() => {
        this.EditForm_show = false
        this.EditForm_doing = false
        this.featchTableData()
      }).catch((error) => {
        this.EditForm_doing = false
        this.$Message.error(error)
      })
    },
    delete: function(index) {
      var data = this.tableData[index]
      client.post('/topic/delete', {'id': topic.id}).then(() => {
        this.featchTableData()
      }).catch((error) => {
        this.$Message.error(error)
      })
    },
  },
})
