<template>
  <div class="student-container">
    <!-- 搜索和操作区域 -->
    <div class="header-actions">
      <div class="search-bar">
        <el-input
            v-model="name"
            placeholder="Please enter the student name"
            prefix-icon="el-icon-search"
            clearable
            class="search-input">
        </el-input>
        <el-button type="primary" @click="load">Search</el-button>
        <el-button type="warning" @click="reset">Reset</el-button>
      </div>

      <div class="operation-buttons">
        <el-button
            type="primary"
            icon="el-icon-plus"
            @click="dialogFormVisible = true">
          Add Student Information
        </el-button>

        <el-popconfirm
            confirm-button-text='OK'
            cancel-button-text='Let me think it over'
            icon="el-icon-info"
            icon-color="red"
            title="Are you sure about deleting this data？"
            @confirm="deleteMore">
          <el-button
              slot="reference"
              type="danger"
              icon="el-icon-delete"
              :disabled="!multipleSelection.length">
            Batch deletion
          </el-button>
        </el-popconfirm>
      </div>
    </div>

    <!-- 表格部分 -->
    <el-table
        :data="tableData"
        border
        stripe
        :header-cell-class-name="'headerBg'"
        class="student-table"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="name" label="name"></el-table-column>
      <el-table-column prop="email" label="email"></el-table-column>
      <el-table-column prop="grade" label="grade"></el-table-column>
      <el-table-column prop="gender" label="gender"></el-table-column>
      <el-table-column prop="professional" label="professional"></el-table-column>
      <el-table-column label="Controls" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" size="mini" @click="handleEdit(scope.row)">
            edit <i class="el-icon-edit"></i>
          </el-button>
          <el-popconfirm
              confirm-button-text='OK'
              cancel-button-text='Let me think it over'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure about deleting this data？"
              @confirm="del(scope.row.uid)">
            <el-button type="danger" size="mini" slot="reference">
              delete <i class="el-icon-remove-outline"></i>
            </el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页器 -->
    <el-pagination
        class="pagination"
        background
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[5, 10, 15, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <!-- 弹窗表单 -->
    <el-dialog
        title="Student Information"
        :visible.sync="dialogFormVisible"
        :modal-append-to-body=false
        width="30%"
        @close="listenDialogClose">
      <el-form label-width="130px" size="small" :rules="rules" ref="editForm" :model="form">
        <el-form-item label="Name" prop="name">
          <el-input placeholder="Please enter content" v-model="form.name" clearable></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email" :rules="[
          { required: true, message: 'Please enter email', trigger: 'blur' },
          { validator: validateEmail, trigger: 'blur' }
        ]">
          <el-input placeholder="Please enter content" v-model="form.email" clearable></el-input>
        </el-form-item>
        <el-form-item label="Year of study" prop="grade">
          <el-input placeholder="Please enter content" v-model="form.grade" clearable></el-input>
        </el-form-item>
        <el-form-item label="Degree of study" prop="classes">
          <el-input placeholder="Please enter content" v-model="form.classes" clearable></el-input>
        </el-form-item>
        <el-form-item label="Gender" prop="gender">
          <el-select v-model="form.gender" placeholder="Please select gender" clearable>
            <el-option label="Male" value="man"></el-option>
            <el-option label="Female" value="woman"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Major" prop="professional">
          <el-input placeholder="Please enter content" v-model="form.professional" clearable></el-input>
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
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 15,
      name: "",
      form: {
        id: "",
        name: "",
        email: "",
        grade: "",
        classes: "",
        gender: "",
        professional: ""
      },
      multipleSelection: [],
      dialogFormVisible: false,
      rules: {
        name: [
          { required: true, message: 'Please enter name', trigger: 'blur' }
        ],
        grade: [
          { required: true, message: 'Please enter grade', trigger: 'blur' },
          { validator: this.validateAcademicYear, trigger: 'blur' }
        ],
        classes: [
          { required: true, message: 'Please enter class', trigger: 'blur' }
        ],
        gender: [
          { required: true, message: 'Please select gender', trigger: 'change' }
        ],
        professional: [
          { required: true, message: 'Please enter major', trigger: 'blur' }
        ]
      }
    };
  },
  created() {
    this.load();
  },
  methods: {
    validateAcademicYear(rule, value, callback) {
      const year = Number(value);
      if (!/^\d+$/.test(value) || year < 2024) {
        callback(new Error('Please enter a valid year (≥ 2024)'));
      } else {
        callback();
      }
    },
    load() {
      this.request.get("/student/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },
    validateEmail(rule, value, callback) {
      const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
      if (!value) {
        return callback(new Error('Please enter email'));
      }
      if (!emailPattern.test(value)) {
        return callback(new Error('Please enter the correct email format'));
      }
      callback();
    },
    save() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.request.post("/student", this.form).then(res => {
            if (res.code === '200') {
              this.load();
              this.$message.success("Submit successfully");
              this.dialogFormVisible = false;
            } else {
              this.$message.error("Commit failure");
            }
          });
        } else {
          this.$message.error("Form validation failed. Please check the input");
          return false;
        }
      });
    },
    handleAdd() {
      this.dialogFormVisible = true;
      this.form = {};
    },
    handleEdit(row) {
      this.form = row;
      this.dialogFormVisible = true;
    },
    del(id) {
      this.request.delete("/student/del/" + id).then(res => {
        if (res) {
          this.$message.success("Successfully deleted");
          this.load();
        } else {
          this.$message.error("Deletion failure");
        }
      });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    reset() {
      this.name = "";
      this.load();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.load();
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.load();
    },
    deleteMore() {
      let ids = this.multipleSelection.map(v => v.id);
      this.request.post("/student/del/batch", ids).then(res => {
        if (res.data) {
          this.$message.success("Batch deletion succeeded");
          this.load();
        } else {
          this.$message.error("Batch deletion failed");
        }
      });
    },
    listenDialogClose() {
      this.$refs.editForm.resetFields();
      this.form = {};
    }
  }
};
</script>

<style>
.student-container {
  padding: 20px;
}

/* 头部布局样式 */
.header-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 10px;
}

.search-input {
  width: 300px;
}

.operation-buttons {
  display: flex;
  gap: 10px;
}

/* 表格样式 */
.student-table {
  margin-bottom: 20px;
}

/* 分页器样式 */
.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

/* 表头背景 */
.headerBg {
  background-color: #f5f7fa !important;
}

/* 按钮动效 */
.el-button {
  transition: all 0.3s;
}

.el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 表单样式 */
.el-dialog__body {
  padding: 20px 30px;
}

.el-form-item {
  margin-bottom: 20px;
}

/* 响应式布局 */
@media screen and (max-width: 768px) {
  .header-actions {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }

  .search-bar {
    width: 100%;
  }

  .search-input {
    flex: 1;
  }

  .operation-buttons {
    width: 100%;
    justify-content: flex-end;
  }
}
</style>