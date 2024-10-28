<template>
  <div>



    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" ></el-table-column>
      <el-table-column prop="uid" label="ID" w></el-table-column>
      <el-table-column prop="username" label="用户名" ></el-table-column>
      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-popconfirm
              confirm-button-text='OK'
              cancel-button-text='Let me think it over.'
              icon="el-icon-info"
              icon-color="red"
              title="Are you sure about deleting this data？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
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
      username: "",
      nickname: "",
      form: {},
      multipleSelection: [],
      dialogAdvice:false,
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/user/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          nickname: this.nickname,


        }
      }).then(res => {
        console.log(res)
        this.tableData = res.data.records
        console.log(res.data.records)
        this.total = res.data.total

      })
    },
    save() {
      this.request.post("/game", this.form).then(res => {
        if (res) {
          this.$message.success("Release success")
          this.dialogAdvice = false
          this.load()
        } else {
          this.$message.error("Publishing failure")
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      console.log(row)
      this.form = row
      this.dialogFormVisible = true
    },
    del(id) {
      console.log(id)
      this.request.delete("/user/" + id).then(res => {
        if (res) {
          this.$message.success("Successfully deleted")
          this.load()
        } else {
          this.$message.error("Deletion failure")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },

    reset() {
      this.userRealname = ""
      this.load()
    },
    handleAdvice(){
      this.dialogAdvice = true
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
