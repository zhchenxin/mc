Vue.component('customer', {
  template: `
    <div>
			<!- 功能栏 -->
			<el-row>
			  <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh" size="small"></el-button>
			  <el-button type="primary" icon="el-icon-plus" @click="handleAdd" size="small"></el-button>
			</el-row>

      <!- 表格 -->
			<el-row>
				<el-table v-loading="tableLoading" :data="tableData" style="width: 100%">
					<el-table-column prop="id" label="ID" />
					<el-table-column prop="topic.name" label="topic" />
					<el-table-column prop="name" label="名称" />
					<el-table-column prop="api" label="api" />
					<el-table-column prop="timeout" label="超时时间" />
					<el-table-column prop="attempts" label="重试次数" />
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
			  <el-form :model="addForm" label-width="80px" label-position="right">
			  	<el-form-item label="topic">
				  	<el-select v-model="addForm.topicId" filterable placeholder="请选择">
					    <el-option v-for="item in addFormTopicList" :key="item.id" :label="item.name" :value="item.id">
					    </el-option>
					  </el-select>
					</el-form-item>
			    <el-form-item label="名称">
			      <el-input v-model="addForm.name" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="api">
			      <el-input v-model="addForm.api" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="超时时间">
			      <el-input v-model="addForm.timeout" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="重试次数">
			      <el-input v-model="addForm.attempts" autocomplete="off"></el-input>
			    </el-form-item>
			  </el-form>
			  <div slot="footer" class="dialog-footer">
			    <el-button @click="addFormVisible = false">取 消</el-button>
			    <el-button type="primary" :loading="addFormLoading" @click="handleAddSave()">确 定</el-button>
			  </div>
			</el-dialog>

			<!-- 修改表单 -->
			<el-dialog title="编辑" :visible.sync="editFormVisible">
			  <el-form :model="editForm" label-width="80px" label-position="right">
			    <el-form-item label="名称">
			      <el-input v-model="editForm.name" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="api">
			      <el-input v-model="editForm.api" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="超时时间">
			      <el-input v-model="editForm.timeout" autocomplete="off"></el-input>
			    </el-form-item>
			    <el-form-item label="重试次数">
			      <el-input v-model="editForm.attempts" autocomplete="off"></el-input>
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
      addFormTopicList: [],

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
    handleRefresh: async function() {
    	this.tableLoading = true
    	try {
    		var tableData = await client.get('/customer', {
	        params:{
	          page: this.currentPage, 
	          limit: this.limit,
	        }
      	})
        this.totalCount = tableData.data.data.mate.total
        this.currentPage = tableData.data.data.mate.currentPage
      	tableData = tableData.data.data.list

        if (tableData.length == 0) {
          this.tableData = tableData
          this.tableLoading = false
          return
        }

      	// 获取 topic 信息
    		var topicIds = []
    		for (var i = tableData.length - 1; i >= 0; i--) {
    			topicIds.push(tableData[i].topicId)
    		}
    		topicIds = unique(topicIds)
    		var topicList = await client.get('/topic', {
					params:{
						id: topicIds.join(','),
	          limit: topicIds.length,
	        }
    		})
    		topicList = topicList.data.data.list

				// 拼接数据
				for (var i = tableData.length - 1; i >= 0; i--) {
    			for (var j = topicList.length - 1; j >= 0; j--) {
    				if (tableData[i].topicId == topicList[j].id) {
    					tableData[i]["topic"] = topicList[j]
    				}
    			}
    		}
        this.tableData = tableData
        this.tableLoading = false
    	} catch(error) {
        this.$message.error(error)
        this.tableData = []
        this.tableLoading = false
    	}
    },
    handleAdd: async function() {
    	try {
	    	this.addForm = {}
	    	this.addFormVisible = true
	    	var topicList = await client.get('/topic', {params:{limit: 10000}})
    		topicList = topicList.data.data.list
    		this.addFormTopicList = topicList
    	} catch(error) {
        this.$message.error(error)
    	}
    },
    handleEdit: function(index, row) {
    	this.editIndex = index
    	this.editForm = {name: row.name, api: row.api, timeout: row.timeout, attempts: row.attempts}
    	this.editFormVisible = true
    },
    handleDelete: function(index, row) {
			client.delete('/customer/' + row.id).then(() => {
        this.handleRefresh()
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    handleAddSave: function() {
    	this.addFormLoading = true
			client.post('/customer', this.addForm).then(() => {
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
			client.put('/customer/' + this.tableData[this.editIndex].id, this.editForm).then(() => {
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