<template>
  <div class="tuition-container">
    <!-- 搜索和操作区域 -->
    <div class="header-actions">
      <div class="search-bar">
        <el-input
            v-model="studentName"
            placeholder="Please enter student name"
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
          New Tuition Bill
        </el-button>

        <el-popconfirm
            title="Are you sure to delete these items?"
            confirm-button-text='Confirm'
            cancel-button-text='Cancel'
            icon="el-icon-warning"
            icon-color="#ff4949"
            @confirm="deleteMore">
          <el-button
              slot="reference"
              type="danger"
              icon="el-icon-delete"
              :disabled="!multipleSelection.length">
            Batch Delete
          </el-button>
        </el-popconfirm>
      </div>
    </div>

    <!-- 数据表格 -->
    <el-table
        :data="tableData"
        border
        stripe
        class="tuition-table"
        @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="studentName" label="Student Name"></el-table-column>
      <el-table-column prop="studentEmail" label="Email"></el-table-column>
      <el-table-column prop="major" label="Major"></el-table-column>
      <el-table-column prop="academicYear" label="Academic Year"></el-table-column>
      <el-table-column prop="totalFee" label="Total Fee">
        <template slot-scope="scope">
          $ {{ scope.row.totalFee }}
        </template>
      </el-table-column>
      <el-table-column label="Operations" width="220" align="center">
        <template slot-scope="scope">
          <el-button
              type="primary"
              size="mini"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)">
            Edit
          </el-button>
          <el-popconfirm
              title="Are you sure to delete this item?"
              confirm-button-text='Confirm'
              cancel-button-text='Cancel'
              icon="el-icon-warning"
              icon-color="#ff4949"
              @confirm="del(scope.row.id)">
            <el-button
                slot="reference"
                type="danger"
                size="mini"
                icon="el-icon-delete">
              Delete
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
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>

    <!-- 表单弹窗 -->
    <el-dialog
        :title="form.id ? 'Edit Tuition Bill' : 'New Tuition Bill'"
        :visible.sync="dialogFormVisible"
        width="600px"
        @close="listenDialogClose">
      <el-form
          ref="editForm"
          :model="form"
          label-width="150px"
          :rules="rules">
        <el-form-item label="Student" prop="studentName">
          <el-select
              v-model="form.studentName"
              placeholder="Select student"
              style="width: 100%"
              @focus="getName"
              @change="selectChange">
            <el-option
                v-for="(item, index) in nameList"
                :key="index"
                :label="item"
                :value="index">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="Email">
          <el-input v-model="form.studentEmail" disabled></el-input>
        </el-form-item>

        <el-form-item label="Major">
          <el-input v-model="form.major" disabled></el-input>
        </el-form-item>

        <el-form-item label="Academic Year" prop="academicYear">
          <el-input v-model="form.academicYear"></el-input>
        </el-form-item>

        <div class="fees-section">
          <h3>Fee Details</h3>
          <el-form-item label="*Tuition Fee" prop="tuitionFee">
            <el-input-number
                v-model="form.tuitionFee"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%">
            </el-input-number>
          </el-form-item>

          <el-form-item label="*Accommodation Fee" prop="accommodationFee">
            <el-input-number
                v-model="form.accommodationFee"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%">
            </el-input-number>
          </el-form-item>

          <el-form-item label="Book Fee" prop="bookFee">
            <el-input-number
                v-model="form.bookFee"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%">
            </el-input-number>
          </el-form-item>

          <el-form-item label="Material Fee" prop="materialFee">
            <el-input-number
                v-model="form.materialFee"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%">
            </el-input-number>
          </el-form-item>

          <el-form-item label="Activity Fee" prop="activityFee">
            <el-input-number
                v-model="form.activityFee"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%">
            </el-input-number>
          </el-form-item>

          <el-form-item label="Exam Fee" prop="examFee">
            <el-input-number
                v-model="form.examFee"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%">
            </el-input-number>
          </el-form-item>

          <el-form-item label="*Total Fee" prop="totalFee">
            <el-input-number
                v-model="form.totalFee"
                :min="0"
                :precision="2"
                :controls="false"
                style="width: 100%"
                disabled>
            </el-input-number>
          </el-form-item>
        </div>
      </el-form>

      <div slot="footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button type="primary" @click="save">Confirm</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "TuitionInvoice",
  data() {
    // 计算总费用的方法
    const calculateTotal = (item) => {
      const fees = ['tuitionFee', 'accommodationFee', 'bookFee', 'materialFee', 'activityFee', 'examFee'];
      return fees.reduce((total, fee) => total + (Number(item[fee]) || 0), 0);
    };

    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      studentName: "",
      studentId: "",
      form: {
        email: '',
        major: '',
      },
      multipleSelection: [],
      nameList: [],
      infoList: [],
      dialogFormVisible: false,
      rules: {
        studentName: [
          { required: true, message: 'Please select a student', trigger: 'change' }
        ],
        academicYear: [
          { required: true, message: 'Please enter academic year', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/tuitionInvoice/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          studentName: this.studentName,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    save() {
      this.$refs.editForm.validate((valid) => {
        if (valid) {
          this.form.status = 0
          const now = new Date()
          const year = now.getFullYear()
          const month = String(now.getMonth() + 1).padStart(2, '0')
          const day = String(now.getDate()).padStart(2, '0')
          const hours = String(now.getHours()).padStart(2, '0')
          const minutes = String(now.getMinutes()).padStart(2, '0')
          this.form.createdTime = `${year}-${month}-${day} ${hours}:${minutes}`

          this.request.post("/tuitionInvoice", this.form).then(res => {
            if (res.code === '200') {
              if (res.data.notify === 0) {
                const data = {
                  uid: res.data.uid,
                  content: "Your tuition bill has been released, please pay it in time!"
                }
                this.request.post("/notify", data).then(() => {
                  this.load()
                  this.dialogFormVisible = false
                  this.$message.success('Bill created and notification sent')
                })
              } else {
                this.load()
                this.dialogFormVisible = false
                this.$message.success('Bill created successfully')
              }
            } else {
              this.$message.error("Operation failed")
            }
          })
        }
      })
    },
    handleEdit(row) {
      this.form = {...row}
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/tuitionInvoice/del/" + id).then(res => {
        if (res) {
          this.$message.success("Successfully deleted")
          this.load()
        } else {
          this.$message.error("Delete failed")
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    reset() {
      this.studentName = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    deleteMore() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning("Please select items to delete")
        return
      }
      const ids = this.multipleSelection.map(v => v.id)
      this.request.post("/tuitionInvoice/del/batch", ids).then(res => {
        if (res.data) {
          this.$message.success("Batch deletion successful")
          this.load()
        } else {
          this.$message.error("Batch deletion failed")
        }
      })
    },
    listenDialogClose() {
      this.$refs.editForm && this.$refs.editForm.resetFields()
      this.form = {}
    },
    getName() {
      this.request.get("/student/list").then(res => {
        this.infoList = res.data
        this.nameList = res.data.map(item => item.name)
      })
    },
    selectChange(index) {
      const selectedStudent = this.infoList[index]
      this.studentId = selectedStudent.id
      this.form.studentEmail = selectedStudent.email
      this.form.major = selectedStudent.professional
      this.form.studentName = selectedStudent.name
    }
  },
  watch: {
    form: {
      handler(newVal) {
        const total = ['tuitionFee', 'accommodationFee', 'bookFee', 'materialFee', 'activityFee', 'examFee']
            .reduce((sum, fee) => sum + (Number(newVal[fee]) || 0), 0)
        this.form.totalFee = Number(total.toFixed(2))
      },
      deep: true
    }
  }
}
</script>

<style scoped>
.tuition-container {
  padding: 20px;
}

/* 修改后的头部布局样式 */
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

.tuition-table {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.fees-section {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.fees-section h3 {
  margin-bottom: 20px;
  color: #606266;
}

.headerBg {
  background-color: #f5f7fa !important;
}

.el-input-number {
  width: 100%;
}

.el-button {
  transition: all 0.3s;
}

.el-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>