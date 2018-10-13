Vue.component('topic', {
  template: `
		<div>
			<!- 功能栏 -->
			<el-row>
			  <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh" size="small"></el-button>
			  <el-button type="primary" icon="el-icon-plus" @click="handleAdd" size="small"></el-button>
			</el-row>
			<el-row>
				<el-table v-loading="tableLoading" :data="tableData" style="width: 100%">
					<el-table-column prop="id" label="ID" />
					<el-table-column prop="name" label="名称" />
					<el-table-column prop="description" label="描述" />
			    <el-table-column prop="createDate" label="创建日期" width="160" />
			    <el-table-column prop="updateDate" label="更新日期" width="160" />
			    <el-table-column label="操作" width="160">
			      <template slot-scope="scope">
			        <el-button size="mini" icon="el-icon-edit" @click="handleEdit(scope.$index, scope.row)"></el-button>
			        <el-button size="mini" icon="el-icon-delete" type="danger" @click="handleDelete(scope.$index, scope.row)"></el-button>
			      </template>
			    </el-table-column>
			  </el-table>
			  <el-pagination
		      @size-change="handleSizeChange"
		      @current-change="handleCurrentChange"
		      :current-page="currentPage"
		      :page-sizes="[50, 100, 250, 500]"
		      :page-size="limit"
		      :total="totalCount"
		      layout="total, sizes, prev, pager, next" >
		    </el-pagination>
			</el-row>

			<!-- 添加表单 -->
			<el-dialog title="添加" :visible.sync="addFormVisible">
			  <el-form :model="addForm" label-width="60px" label-position="right">
			    <el-form-item label="名称">
			      <el-input v-model="addForm.name" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="描述">
			      <el-input v-model="addForm.description" autocomplete="off"></el-input>
			    </el-form-item>
			  </el-form>
			  <div slot="footer" class="dialog-footer">
			    <el-button @click="addFormVisible = false">取 消</el-button>
			    <el-button type="primary" :loading="addFormLoading" @click="handleAddSave()">确 定</el-button>
			  </div>
			</el-dialog>

			<!-- 修改表单 -->
			<el-dialog title="编辑" :visible.sync="editFormVisible">
			  <el-form :model="editForm" label-width="60px" label-position="right">
			    <el-form-item label="名称">
			      <el-input v-model="editForm.name" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="描述">
			      <el-input v-model="editForm.description" autocomplete="off"></el-input>
			    </el-form-item>
			  </el-form>
			  <div slot="footer" class="dialog-footer">
			    <el-button @click="editFormVisible = false">取 消</el-button>
			    <el-button type="primary" :loading="editFormLoading" @click="handleEditSave()">确 定</el-button>
			  </div>
			</el-dialog>
		</div>
  `,
  data: function () {
    return {
    	// 表格
    	tableLoading: false,
      tableData: [],

      // 分页
      totalCount: 0,
      currentPage: 1,
      limit: 50,

      // 添加表单
      addFormVisible: false,
      addFormLoading: false,
      addForm: {},

      // 修改表单
      editFormVisible: false,
      editFormLoading: false,
      editIndex: 0,
      editForm: {},
    }
  },
  created: function () {
    this.handleRefresh()
  },
  methods: {
    handleSizeChange: function(size) {
    	this.limit = size
    	this.handleRefresh()
    },
    handleCurrentChange: function(page) {
    	this.currentPage = page
    	this.handleRefresh()
    },
    handleRefresh: function() {
    	this.tableLoading = true
    	client.get('/topic', {
        params:{
          page: this.currentPage, 
          limit: this.limit,
        }
      }).then((response) => {
        this.tableData = response.data.data.list
        this.totalCount = response.data.data.mate.total
        this.currentPage = response.data.data.mate.currentPage
        this.tableLoading = false
      }).catch((error) => {
        this.$message.error(error)
        this.tableData = []
        this.tableLoading = false
      })
    },
    handleAdd: function() {
    	this.addForm = {}
    	this.addFormVisible = true
    },
    handleEdit: function(index, row) {
    	this.editIndex = index
    	this.editForm = {name: row.name, description: row.description}
    	this.editFormVisible = true
    },
    handleDelete: function(index, row) {
			client.delete('/topic/' + row.id).then(() => {
        this.handleRefresh()
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    handleAddSave: function() {
    	this.addFormLoading = true
			client.post('/topic', this.addForm).then(() => {
        this.addFormLoading = false
        this.addFormVisible = false
        this.handleRefresh()
      }).catch((error) => {
        this.addFormLoading = false
        this.$message.error(error)
      })
    },
    handleEditSave: function() {
    	this.editFormLoading = true
			client.put('/topic/' + this.tableData[this.editIndex].id, this.editForm).then(() => {
        this.editFormLoading = false
        this.editFormVisible = false
        this.handleRefresh()
      }).catch((error) => {
        this.editFormLoading = false
        this.$message.error(error)
      })
    },
  },
})
