<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="Please enter the student name" suffix-icon="el-icon-search" v-model="studentName"></el-input>

      <el-button class="ml-5" type="primary" @click="load">search</el-button>
      <el-button type="warning" @click="reset">reset</el-button>
    </div>
    <div style="margin: 10px 0">
      <el-button type="primary" size="medium" style="width: 160px; margin-bottom: 20px" @click="dialogFormVisible = true">New tuition bill</el-button>

      <el-popconfirm
          confirm-button-text='OK'
          cancel-button-text='Let me think it over.'
          icon="el-icon-info"
          icon-color="red"
          title="Are you sure about deleting this data？"
          @confirm="deleteMore"
      >
        <el-button type="danger" size="medium" style="width: 130px; margin-left: 10px" slot="reference">  Batch deletion  </el-button>
      </el-popconfirm>

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection"></el-table-column>
      <el-table-column prop="id" label="ID"></el-table-column>
      <el-table-column prop="studentName" label="studentName"></el-table-column>
      <el-table-column prop="studentEmail" label="studentEmail"></el-table-column>
      <el-table-column prop="major" label="major"></el-table-column>
      <el-table-column prop="academicYear" label="academicYear"></el-table-column>
      <el-table-column prop="totalFee" label="totalFee"></el-table-column>
      <el-table-column label="editQ" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="success" @click="handleEdit(scope.row)">EDIT <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              confirm-button-text='OK'
              cancel-button-text='Let me think it over.'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure about deleting this data？"
              @confirm="del(scope.row.id)"
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

      <el-dialog title="Tuition sheet information" :visible.sync="dialogFormVisible" :modal-append-to-body=false width="30%" @close="listenDialogClose">
      <el-form label-width="190px" size="small"  ref="editForm" :model="form">
        <el-form-item label="Select a user" prop="aid">
          <el-select v-model="form.studentName" placeholder="Please select" style="width: 300px" @focus="getName" @change="selectChange">
            <el-option
                v-for="(item, index)  in nameList"
                :key="index"
                :label="item"
                :value="index"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="Email" prop="email">
          <el-input
              disabled
              v-model="form.studentEmail"
              clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="Major" prop="major">
          <el-input
              disabled
              v-model="form.major"
              clearable>
          </el-input>
        </el-form-item>
        <el-form-item label="*Year of Study" prop="academicYear">
          <el-input
              placeholder="Please enter the Year of Study"
              v-model="form.academicYear"
              clearable>
          </el-input>
        </el-form-item>

        <el-form-item label="*Tuition Fee" prop="tuitionFee">
          <el-input-number v-model="form.tuitionFee" :min="0" :controls="false" :precision="2"></el-input-number>
        </el-form-item>

        <el-form-item label="Accommodation Fee" prop="accommodationFee">
          <el-input-number v-model="form.accommodationFee" :min="0" :controls="false" :precision="2"></el-input-number>

        </el-form-item>

        <el-form-item label="Book Fee" prop="bookFee">
          <el-input-number v-model="form.bookFee" :min="0" :controls="false" :precision="2"></el-input-number>

        </el-form-item>

        <el-form-item label="Material Fee" prop="materialFee">
          <el-input-number v-model="form.materialFee" :min="0" :controls="false" :precision="2"></el-input-number>

        </el-form-item>

        <el-form-item label="Activity Fee" prop="activityFee">
          <el-input-number v-model="form.activityFee" :min="0" :controls="false" :precision="2"></el-input-number>

        </el-form-item>

        <el-form-item label="Exam Fee" prop="examFee">
          <el-input-number v-model="form.examFee" :min="0" :controls="false" :precision="2"></el-input-number>

        </el-form-item>

        <el-form-item label="*Total Fee" prop="totalFee">
          <el-input-number v-model="form.totalFee" :min="0" :controls="false" :precision="2"></el-input-number>

        </el-form-item>
<!--        <el-form-item label="Payment Amount" prop="paymentAmount">-->
<!--          <el-input-number v-model="form.paymentAmount" :min="0" :controls="false" :precision="2"></el-input-number>-->

<!--        </el-form-item>-->


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">Cancel</el-button>
        <el-button notiftypey="primary" @click="save">Apply</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "TuitionInvoice",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 2,
      studentName: "",
      studentId: "",
      form: {
        email:'',
        major:'',
      },
      multipleSelection: [],
      nameList: [],
      infoList: [],
      dialogFormVisible: false,
    };
  },
  created() {
    this.load();
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
        console.log(res);
        this.tableData = res.data.records;
        this.total = res.data.total;
      });
    },
    save() {
      this.form.status = 0
      const now = new Date();
      const year = now.getFullYear();
      const month = String(now.getMonth() + 1).padStart(2, '0');
      const day = String(now.getDate()).padStart(2, '0');
      const hours = String(now.getHours()).padStart(2, '0');
      const minutes = String(now.getMinutes()).padStart(2, '0');
      this.form.createdTime = `${year}-${month}-${day} ${hours}:${minutes}`
      console.log(this.form)
      this.request.post("/tuitionInvoice", this.form).then(res => {
        console.log(res)
        if (res.code === '200') {
          if (res.data.notify===0){
            var data = {
              uid:res.data.uid,
              content:"Your tuition bill has been released, please pay it in time！"
            }
            this.request.post("/notify", data).then(res => {
              this.load();
              this.dialogFormVisible = false;
            })
          }
          else {
            this.load();
            this.dialogFormVisible = false;
          }
        } else {
          this.$message.error("Commit failure");
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
      this.request.delete("/tuitionInvoice/del/" + id).then(res => {
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
      this.studentName = "";
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
      this.request.post("/tuitionInvoice/del/batch", ids).then(res => {
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
    },
    getName() {
      this.request.get("/student/list").then(res => {
        console.log(res)
        this.infoList = res.data
        this.nameList = res.data.map(item => item.name);
        console.log(this.nameList)
        console.log(this.infoList)
      })
    },
    selectChange(index){
      this.studentId = this.infoList[index].id
      this.form.studentEmail=this.infoList[index].email
      this.form.major=this.infoList[index].professional
      this.form.studentName = this.infoList[index].name
    },
    validateFee() {
      // 只允许输入数字并且最多两位小数
      const value = this.form.totalFee;
      const regex = /^\d*(\.\d{0,2})?$/;
      if (!regex.test(value)) {
        // 如果输入不合法，则去掉最后一个字符
        this.form.totalFee = value.slice(0, -1);
      }
    }
  }
};
</script>

<style>
.headerBg {
  background: #eee !important;
}
</style>
