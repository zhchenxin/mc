Vue.component('failed_message', {
  template: `
    <div>
    	<!- 搜索栏 -->
			<el-row>
				<el-form :inline="true" :model="searchForm" class="demo-form-inline">
				  <el-form-item label="topic">
				  	<el-select v-model="searchForm.customerId" filterable clearable >
					    <el-option v-for="item in allCustomerList" :key="item.id" :label="item.name" :value="item.id"></el-option>
					  </el-select>
					</el-form-item>
				  <el-form-item>
				    <el-button type="primary" @click="handleRefresh">查询</el-button>
				  </el-form-item>
				</el-form>			
			</el-row>

    	<!- 功能栏 -->
			<el-row>
			  <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh" size="small"></el-button>
			  <el-button type="primary" @click="handleRetryAll" size="small">重试</el-button>
			  <el-button type="danger" @click="handleDeleteAll" size="small">删除</el-button>
			</el-row>

			<!- 表格 -->
			<el-row>
				<el-table v-loading="tableLoading" :data="tableData" style="width: 100%" @selection-change="handleSelectionChange">
					<el-table-column type="expand">
            <template slot-scope="props">
              <el-form label-width="80px" label-position="left">
                <el-form-item label="请求"">
                  <pre>{{ props.row.request }}</pre>
                </el-form-item>
                <el-form-item label="响应"">
                  <pre>{{ props.row.response }}</pre>
                </el-form-item>
                <el-form-item label="错误"">
                  <pre>{{ props.row.error }}</pre>
                </el-form-item>
              </el-form>
            </template>
          </el-table-column>
					<el-table-column type="selection"></el-table-column>
					<el-table-column prop="id" label="ID" />
					<el-table-column prop="topic.name" label="topic" />
					<el-table-column prop="customer.name" label="消费者名称" />
					<el-table-column prop="request" label="请求" show-overflow-tooltip/>
					<el-table-column prop="response" label="响应" show-overflow-tooltip/>
			    <el-table-column prop="error" label="错误信息" width="160" show-overflow-tooltip/>
			    <el-table-column prop="createDate" label="创建时间" width="160" />
			    <el-table-column label="操作" width="240">
			      <template slot-scope="scope">
              <el-button size="mini" type="primary" @click="handleLog(scope.$index, scope.row)">日志</el-button>
			        <el-button size="mini" type="primary" @click="handleRetry(scope.$index, scope.row)">重试</el-button>
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

      <!- 进度展示 -->
      <el-dialog title="进度" :visible.sync="progressDialogVisible" width="30%">
        <div>总数量: {{ progressTotal }}</div>
        <div>已执行数量: {{ progressNum }}</div>
        <el-progress :text-inside="true" :stroke-width="18" :percentage="percentage" status="exception"></el-progress>
      </el-dialog>

      <!-- 详情日志 -->
      <el-dialog title="请求日志" :visible.sync="logDialogVisible" fullscreen>
        <el-card v-for="log in messageLogs" shadow="never" >
           <el-form label-width="80px" label-position="left">
              <el-form-item label="请求时间"">
                <pre>{{ log.createDate }}</pre>
              </el-form-item>
              <el-form-item label="响应"">
                <pre>{{ log.response }}</pre>
              </el-form-item>
            </el-form>
        </el-card>
      </el-dialog>
    </div>
  `,
  data: function () {
    return {
    	// 通用数据
    	allCustomerList: [],

    	// 搜索栏
      searchForm: {
      	customerId: '',
      },

      // 表格
    	tableLoading: false,
      tableData: [],
      selection: [],
      

      // 分页
      totalCount: 0,
      currentPage: 1,
      limit: 50,

      // 进度展示
      progressDialogVisible: false,
      progressTotal: 0,
      progressNum: 0,

      // 详情日志
      logDialogVisible: false,
      messageLogs: [],
    }
  },
  computed: {
    percentage: function() {
      return Math.floor(this.progressNum * 100 / this.progressTotal)
    },
  },
  created: async function () {
    this.handleRefresh()

		// 初始化基本数据
    try {
    	var customerList = await client.get('/customer', {params:{limit: 10000}})
  		customerList = customerList.data.data.list
  		this.allCustomerList = customerList
  	} catch(error) {
      this.$message.error(error)
  	}
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
    handleSelectionChange: function(val) {
    	this.selection = val
    },
    handleRefresh: async function() {
    	this.tableLoading = true
    	try {
    		var tableData = await client.get('/failed_message', {
	        params:{
	        	customerId: this.searchForm.customerId,
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

    		// 获取 customer 信息
    		var customerIds = []
    		for (var i = tableData.length - 1; i >= 0; i--) {
    			customerIds.push(tableData[i].customerId)
    		}
    		customerIds = unique(customerIds)
    		var customerList = await client.get('/customer', {
					params:{
						id: customerIds.join(','),
	          limit: customerIds.length,
	        }
    		})
    		customerList = customerList.data.data.list

				// 拼接数据
				for (var i = tableData.length - 1; i >= 0; i--) {
    			for (var j = topicList.length - 1; j >= 0; j--) {
    				if (tableData[i].topicId == topicList[j].id) {
    					tableData[i]["topic"] = topicList[j]
    				}
    			}
    			for (var j = customerList.length - 1; j >= 0; j--) {
    				if (tableData[i].customerId == customerList[j].id) {
    					tableData[i]["customer"] = customerList[j]
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
    handleLog: function(index, row) {
      this.logDialogVisible = true
      client.get('/message_log', {params:{messageId: row.id,limit: 100}}).then((response) => {
        this.messageLogs = response.data.data.list
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    handleRetry: function(index, row) {
    	client.put('/failed_message/' + row.id + '/retry').then(() => {
        this.handleRefresh()
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    handleDelete: function(index, row) {
			client.delete('/failed_message/' + row.id).then(() => {
        this.handleRefresh()
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    handleRetryAll: async function() {
      this.progressTotal = this.selection.length
      this.progressNum = 0
      this.progressDialogVisible = true
    	for (var i = this.selection.length - 1; i >= 0; i--) {
    		await client.put('/failed_message/' + this.selection[i].id + '/retry')
        this.progressNum++
    	}
    	this.handleRefresh()
      this.progressDialogVisible = false
    },
    handleDeleteAll: async function() {
      this.progressTotal = this.selection.length
      this.progressNum = 0
      this.progressDialogVisible = true
			for (var i = this.selection.length - 1; i >= 0; i--) {
    		await client.delete('/failed_message/' + this.selection[i].id)
        this.progressNum++
    	}
    	this.handleRefresh()
       this.progressDialogVisible = false
    },
  },
})