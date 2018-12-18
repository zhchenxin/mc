<template>
  <div>
    <!-- 功能栏 -->
    <el-row>
      <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh" size="small"></el-button>
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd" size="small"></el-button>
    </el-row>

    <!-- 表格 -->
    <el-row>
      <el-table v-loading="tableLoading" :data="tableData" style="width: 100%">
        <el-table-column prop="id" label="ID" />
        <el-table-column prop="topic.name" label="topic" />
        <el-table-column prop="name" label="名称" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="spec" label="spec" />
        <el-table-column prop="updateDate" label="更新日期" width="160" />
        <el-table-column prop="createDate" label="创建日期" width="160" />
        <el-table-column label="状态" width="70">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status == 1" type="success" size="mini"  @click="handleStatusChange(scope.$index, scope.row)">暂停</el-button>
            <el-button v-if="scope.row.status == 2" type="info" size="mini" @click="handleStatusChange(scope.$index, scope.row)">启动</el-button>            
          </template>
        </el-table-column>
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
        <el-form-item label="topic">
          <el-select v-model="addForm.topicId" filterable placeholder="请选择">
            <el-option v-for="item in addFormTopicList" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="addForm.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="addForm.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="spec">
          <el-input v-model="addForm.spec" autocomplete="off"></el-input>
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
        <el-form-item label="spec">
          <el-input v-model="editForm.spec" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editFormVisible = false">取 消</el-button>
        <el-button type="primary" :loading="editFormLoading" @click="handleEditSave()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>

import client from '../utils/Client.js'
import utils from '../utils/Utils.js'

export default {
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
        var tableData = await client.get('/cron', {
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
        topicIds = utils.unique(topicIds)
        var topicList = await client.get('/topic', {
          params:{
            id: topicIds.join(','),
            limit: topicIds.length,
          }
        })
        topicList = topicList.data.data.list

        // 拼接数据
        for (i = tableData.length - 1; i >= 0; i--) {
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
      this.editIndex = index;
      this.editForm = {name: row.name, description: row.description, spec: row.spec}
      this.editFormVisible = true
    },
    handleStatusChange: function(index, row) {
      var url = '/cron/' + row.id + '/start'
      if (row.status == 1) {
        url = '/cron/' + row.id + '/stop'
      }
      client.put(url).then(() => {
        this.handleRefresh()
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    handleDelete: function(index, row) {
      client.delete('/cron/' + row.id).then(() => {
        this.handleRefresh()
      }).catch((error) => {
        this.$message.error(error)
      })
    },
    handleAddSave: function() {
      this.addFormLoading = true
      client.post('/cron', this.addForm).then(() => {
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
      client.put('/cron/' + this.tableData[this.editIndex].id, this.editForm).then(() => {
        this.editFormLoading = false
        this.editFormVisible = false
        this.handleRefresh()
      }).catch((error) => {
        this.editFormLoading = false
        this.$message.error(error)
      })
    },
  },
}
</script>

<style scoped>
</style>
