Vue.component('cron', {
  template: `
    <div>
      <Spin size="large" fix v-if="loading"></Spin>
      <Row>
        <Breadcrumb>
          <BreadcrumbItem to="/">Home</BreadcrumbItem>
          <BreadcrumbItem>定时任务</BreadcrumbItem>
        </Breadcrumb>
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
            <Page :total="totalCount" :current="currentPage" show-sizer :page-size="25" :page-size-opts="[25, 50, 100, 250, 500]" size="small" show-total @on-change="changePage" @on-page-size-change="changePageSize"></Page>
          </div>
        </div>
      </Row>

      <Modal v-model="AddForm_show" :footer-hide="true" title="添加">
        <div>
          <Form ref="AddForm_formData" :model="AddForm_formData" :label-width="80">
            <FormItem label="名称" prop="name">
              <Input v-model="AddForm_formData.name" placeholder="请输入名称"/>
            </FormItem>
            <FormItem label="Topic" prop="topicId">
              <Select v-model="AddForm_formData.topicId">
                  <Option v-for="item in AddForm_topicList" :value="item.id" :key="item.id">{{ item.name }}</Option>
              </Select>
            </FormItem>
            <FormItem label="描述" prop="description">
              <Input v-model="AddForm_formData.description"/>
            </FormItem>
            <FormItem label="spec" prop="spec">
              <Input v-model="AddForm_formData.spec"/>
            </FormItem>
            <FormItem>
              <Button type="success" :loading="AddForm_doing" @click="add()">提交</Button>
            </FormItem>
          </Form>
        </div>
      </Modal>

      <Modal v-model="EditForm_show" :footer-hide="true" title="编辑">
        <div>
          <Form ref="EditForm_formData" :model="EditForm_formData" :label-width="80">
            <FormItem label="名称" prop="name">
              <Input v-model="EditForm_formData.name" placeholder="请输入名称"/>
            </FormItem>
            <FormItem label="描述" prop="description">
              <Input v-model="EditForm_formData.description"/>
            </FormItem>
            <FormItem label="spec" prop="spec">
              <Input v-model="EditForm_formData.spec"/>
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
      // 表格属性
      loading: false,
      tableColumns: [
        {title: 'id', key: 'id'},
        {title: '名称', key: 'name'},
        {title: '描述', key: 'description'},
        {title: 'topic', key: 'topicName'},
        {title: 'spec', key: 'spec'},
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

      // 表格列表
      tableData: [],

      // 分页
      totalCount: 0,
      currentPage: 1,
      limit: 25,

      // 搜索
      formSearch: {
        topicId: ''
      },
      topic_list: [],

      // 增加表单
      AddForm_show: false,
      AddForm_doing: false,
      AddForm_topicList: [],
      AddForm_formData: {
        name: '',
        description: '',
        spec: '',
        topicId: '',
      },

      // 编辑表单
      EditForm_show: false,
      EditForm_doing: false,
      EditForm_formData: {
        id: 0,
        name: '',
        description: '',
        spec: '',
      },
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
    refresh: function () {
      this.featchTableData()
    },
    search: function() {
      this.featchTableData()
      this.$router.replace({ path: '/cron', query: { topicId: this.formSearch.topicId }})
    },
    featchTableData: function () {
      this.loading = true
      client.get('/cron', {
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
    },
    showAdd: function() {
      client.get('/cron/create').then((response) => {
        this.AddForm_formData = {
          name: '',
          topic_id: '',
          api: '',
          timeout: '',
          attempts: '',
        }
        this.AddForm_topicList = response.data.data.topicList
        this.AddForm_show=true
      }).catch((error) => {
        this.$Message.error(error)
      })
    },
    add: function() {
      this.AddForm_doing = true
      client.post('/cron/create', this.AddForm_formData).then(() => {
        this.AddForm_show = false
        this.AddForm_doing = false
        this.featchTableData()
      }).catch((error) => {
        this.AddForm_doing = false
        this.$Message.error(error)
      })
    },
    showEdit: function(index) {
      var data = this.tableData[index]
      client.get('/cron/update', {params:{id:data.id}}).then((response) => {
        this.EditForm_formData = response.data.data.cron
        this.EditForm_show=true
      }).catch((error) => {
        this.$Message.error(error)
      })
    },
    edit: function() {
      this.EditForm_doing = true
      client.post('/cron/update', this.EditForm_formData).then(() => {
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
      client.post('/cron/delete', {'id': data.id}).then(() => {
        this.featchTableData()
      }).catch((error) => {
        this.$Message.error(error)
      })
    },
  },
})