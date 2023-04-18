<template>
  <div>
    <el-container>
      <el-header>
        <el-row style="height: 100%">
          <el-col
            :span="12"
            style="height: 100%; display: flex; align-items: center"
          >
            <h1 style="font-size: 20px; margin: 0px">
              <span style="color: #f56c6c">已经失败</span>的任务
            </h1>
          </el-col>
          <el-col
            :span="12"
            style="
              display: flex;
              align-items: center;
              justify-content: right;
              height: 100%;
            "
          >
            <span style="font-size: 10px; color: grey; margin-right: 10px"
              >上次刷新时间：{{ lastUpdateTime }}</span
            >
            <el-button
              type="primary"
              icon="el-icon-refresh"
              circle
              @click="getData"
              style="float: right"
            ></el-button>
          </el-col>
        </el-row>
      </el-header>
      <el-main>
        <el-table
          :data="
            tableData.slice(
              (currentPage - 1) * pageSize,
              currentPage * pageSize
            )
          "
          stripe
          @sort-change="sortChange"
        >
          <el-table-column prop="name" label="任务名称" sortable="custom">
          </el-table-column>
          <el-table-column prop="model" label="选用模型" sortable="custom">
          </el-table-column>
          <el-table-column prop="conf" label="置信度" sortable="custom">
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" sortable="custom">
          </el-table-column>
          <el-table-column prop="doneTime" label="失败时间" sortable="custom">
          </el-table-column>
          <el-table-column prop="exception" label="失败原因" sortable="custom">
            <template slot-scope="scope">
              <span style="color: #f56c6c">{{ scope.row.exception }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="comment" label="任务注释" sortable="custom">
          </el-table-column>
          <el-table-column fixed="right" label="操作">
            <template slot-scope="scope">
              <el-button
                type="danger"
                size="small"
                @click="oDelete(scope.row.id)"
                >删除任务</el-button
              >
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="tableData.length"
          :current-page="currentPage"
          :page-size="pageSize"
          :page-sizes="[5, 10, 15, 20]"
          align="center"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        >
        </el-pagination>
      </el-main>
    </el-container>
  </div>
</template>

<script>
import utils from "../utils/utils.js";
export default {
  data() {
    return {
      tableData: [],
      currentPage: 1,
      pageSize: 10,
      lastUpdateTime: "",
    };
  },
  methods: {
    //每页条数改变时触发 选择一页显示多少行
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`);
      this.currentPage = 1;
      this.pageSize = val;
    },
    //当前页改变时触发 跳转其他页
    handleCurrentChange(val) {
      this.currentPage = val;
    },
    sortChange(column) {
      utils.sortChange(column, this.tableData);
    },
    getData() {
      utils.getData(this, "/task/failed_task");
    },
    oDelete(id) {
      this.$confirm("此操作将永久删除该任务, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$axios
            .get(this.$store.state.backendAddress + "/task/delete/" + id)
            .then((res) => {
              if (res.data.code == 200) {
                this.$message({
                  type: "success",
                  message: "删除成功!",
                });
                this.getData();
              } else {
                this.$message.error(this.$store.state.serverErrMsg);
              }
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
  },
  mounted() {
    this.getData();
  },
};
</script>
