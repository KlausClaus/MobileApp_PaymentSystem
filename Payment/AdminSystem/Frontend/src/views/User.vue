<template>
  <div class="user-container">
    <!-- 搜索栏 -->
    <div class="search-container">
      <el-input
          v-model="username"
          placeholder="Search username"
          class="search-input"
          clearable
          prefix-icon="el-icon-search"
          @clear="load"
          @keyup.enter.native="load"
      >
        <el-button slot="append" icon="el-icon-search" @click="load"></el-button>
      </el-input>
      <el-button type="primary" @click="handleAdd" icon="el-icon-plus">Add User</el-button>
    </div>

    <!-- 表格 -->
    <el-table
        :data="tableData"
        border
        stripe
        :header-cell-class-name="'headerBg'"
        @selection-change="handleSelectionChange"
        class="user-table"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="uid" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="Username"></el-table-column>
      <el-table-column label="Operations" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleEdit(scope.row)">
            Edit <i class="el-icon-edit"></i>
          </el-button>
          <el-popconfirm
              confirm-button-text='Confirm'
              cancel-button-text='Cancel'
              icon="el-icon-warning"
              icon-color="red"
              title="Are you sure to delete this user?"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" size="mini" slot="reference">
              Delete <i class="el-icon-delete"></i>
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页器 -->
    <div class="pagination-container">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20, 50]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <!-- 新增/编辑弹窗 -->
    <el-dialog :title="form.id ? 'Edit User' : 'Add User'" :visible.sync="dialogFormVisible" width="30%">
      <el-form :model="form" label-width="90px" :rules="rules" ref="form">
        <el-form-item label="Username" prop="username">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="Password" prop="password" v-if="!form.id">
          <el-input v-model="form.password" show-password></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      // 表格数据
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      // 表单验证规则
      rules: {
        username: [
          { required: true, message: 'Please input username', trigger: 'blur' },
          { min: 3, max: 20, message: 'Length should be 3 to 20 characters', trigger: 'blur' }
        ],
        password: [
          { required: true, message: 'Please input password', trigger: 'blur' },
          { min: 6, max: 20, message: 'Length should be 6 to 20 characters', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    // 加载数据
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    // 保存用户
    save() {
      this.request.post("/user", this.form).then(res => {
        if (res) {
          this.$message.success("Operation successful")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("Operation failed")
        }
      })
    },
    // 新增按钮
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    // 编辑按钮
    handleEdit(row) {
      this.form = row
      this.dialogFormVisible = true
    },
    // 删除
    del(id) {
      this.request.delete("/user/" + id).then(res => {
        if (res) {
          this.$message.success("Deleted successfully")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    // 表格多选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 分页大小改变
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    // 当前页改变
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    }
  }
}
</script>

<style scoped>
.user-container {
  padding: 20px;
}

.search-container {
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
}

.search-input {
  width: 300px;
  margin-right: 10px;
}

.user-table {
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.headerBg {
  background: #f5f7fa !important;
  color: #606266;
  font-weight: bold;
}

.el-button {
  margin-left: 5px;
  transition: all 0.3s;
}

.el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.el-table__row:hover {
  background-color: #f5f7fa !important;
}
</style>