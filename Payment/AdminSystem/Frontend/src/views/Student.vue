<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="Please enter the student name" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">search</el-button>
      <el-button type="warning" @click="reset">reset</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" size="medium" style="width: 160px; margin-bottom: 20px" @click="dialogFormVisible = true">Add Student Information</el-button>

      <el-popconfirm
          confirm-button-text='OK'
          cancel-button-text='Let me think it over.'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure about deleting this data？"
          @confirm="deleteMore"
      >
        <el-button type="danger" size="medium" style="width: 130px; margin-left: 10px" slot="reference">Batch deletion</el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="name" label="name"></el-table-column>
      <el-table-column prop="email" label="email"></el-table-column>
      <el-table-column prop="grade" label="grade"></el-table-column>
      <el-table-column prop="gender" label="gender"></el-table-column>
      <el-table-column prop="professional" label="professional"></el-table-column>
      <el-table-column label="Controls" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">edit <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              confirm-button-text='OK'
              cancel-button-text='Let me think it over.'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure about deleting this data？"
              @confirm="del(scope.row.uid)"
          >
            <el-button type="danger" slot="reference">delete <i class="el-icon-remove-outline"></i></el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="Student Information" :visible.sync="dialogFormVisible" :modal-append-to-body=false width="30%" @close="listenDialogClose">
      <el-form label-width="130px" size="small" :rules="rules" ref="editForm" :model="form">
        <el-form-item label="Name" prop="name">
          <el-input placeholder="Please enter content" v-model="form.name" clearable></el-input>
        </el-form-item>
        <el-form-item label="Email" prop="email" :rules="[{ required: true, message: 'Please enter email', trigger: 'blur' }, { validator: validateEmail, trigger: 'blur' }]">
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
      pageSize: 2,
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
      dialogFormVisible: false
    };
  },
  created() {
    this.load();
  },
  methods: {
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
          // 表单验证通过，执行保存操作
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
          // 表单验证失败，阻止保存操作
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
.headerBg {
  background: #eee !important;
}
</style>
