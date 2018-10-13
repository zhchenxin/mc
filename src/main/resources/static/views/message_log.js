Vue.component('message_log', {
  template: `
    <div>
    	<!- 搜索栏 -->
			<el-row>
				<el-form :inline="true" :model="searchForm" class="demo-form-inline">
				  <el-form-item label="topic">
				  	<el-select v-model="searchForm.customerId" filterable placeholder="请选择">
					    <el-option v-for="item in allCustomerList" :key="item.id" :label="item.name" :value="item.id">
					    </el-option>
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
			</el-row>

			<!- 表格 -->
			<el-row>
				<el-table v-loading="tableLoading" :data="tableData" style="width: 100%">
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
					<el-table-column prop="id" label="ID" />
					<el-table-column prop="topic.name" label="topic" />
					<el-table-column prop="customer.name" label="消费者名称" />
					<el-table-column prop="request" label="请求" show-overflow-tooltip/>
					<el-table-column prop="response" label="响应" show-overflow-tooltip/>
			    <el-table-column prop="error" label="错误信息" width="160" show-overflow-tooltip/>
          <el-table-column prop="time" label="请求时长(ms)" width="160" show-overflow-tooltip/>
			    <el-table-column prop="createDate" label="创建时间" width="160" />
			  </el-table>
			  <el-pagination
		      @size-change="handleSizeChange"
		      @current-change="handleCurrentChange"
		      :current-page="currentPage"
		      :page-sizes="[10, 50, 100, 250, 500]"
		      :page-size="limit"
		      :total="totalCount"
		      layout="total, sizes, prev, pager, next" >
		    </el-pagination>
			</el-row>
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

      // 分页
      totalCount: 0,
      currentPage: 1,
      limit: 10,
    }
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
    handleRefresh: async function() {
    	this.tableLoading = true
    	try {
    		var tableData = await client.get('/message_log', {
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
  },
})