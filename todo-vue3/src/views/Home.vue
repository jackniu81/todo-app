<template>
  <div class="container">
    <div class="home">
      <el-input
        placeholder="Please input a task, press Enter to save"
        @keyup.enter="createTask"
        v-model="input"
      ></el-input>
      <el-divider></el-divider>
      <el-radio-group v-model="taskFilter">
        <el-radio-button label="All"></el-radio-button>
        <el-radio-button label="Completed"></el-radio-button>
        <el-radio-button label="In Progress"></el-radio-button>
      </el-radio-group>
      <el-table
        :data="taskList"
        stripe
        style="width: 100%"
        :show-header="false"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="completed" width="30">
          <template #default="scope">
            <el-checkbox
              v-model="scope.row.completed"
              @change="updateComplete(scope.row)"
            >
              {{ scope.row.completed }}
            </el-checkbox>
          </template>
        </el-table-column>
        <el-table-column prop="content" width="380"> </el-table-column>
        <el-table-column>
          <template #default="scope"
            ><el-tag>{{
              new Date(scope.row.editTime).toLocaleString()
            }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import { Task } from "@/types";

const taskUrl = "http://localhost:8080/todo-api/v1/tasks/";

export default defineComponent({
  name: "Home",
  components: {},
  data() {
    return {
      input: ref(""),
      tasks: new Array<Task>(),
      taskFilter: "All",
    };
  },
  mounted() {
    this.loadTasks();
  },
  methods: {
    loadTasks() {
      this.axios
        .get(taskUrl)
        .then((response) => {
          this.tasks = response.data;
        })
        .catch((err) => console.log("Error loading podcasts: " + err));
    },
    /**
     * get row class for completed task
     */
    tableRowClassName(data: any) {
      const task: Task = data.row;
      return task.completed ? "row-done" : "";
    },
    updateComplete(task: Task) {
      this.axios
        .put(taskUrl + task.id, task)
        .then((response) => {
          console.log("Update Task: ", response.data);
          this.input = ref("");
        })
        .catch((err) => console.log("Update task error: " + err));
    },
    createTask(ev: any) {
      const content = ev.target.value.trim();

      if (!content) return;

      this.axios
        .post(taskUrl, {
          completed: false,
          content: content,
        })
        .then((response) => {
          console.log("Creat Task: ", response.data);
          this.input = ref("");
          return this.loadTasks();
        })
        .catch((err) => console.log("Creat task error: " + err));
    },
  },
  computed: {
    taskList(): Array<Task> {
      const list = this.tasks;
      if (this.taskFilter === "All") return list;
      else
        return list.filter(
          (p: Task) => p.completed == (this.taskFilter == "Completed")
        );
    },
  },
});
</script>

<style scoped>
.home {
  width: 600px;
}
.container {
  display: flex;
  justify-content: center;
}

.el-table /deep/ .row-done {
  text-decoration: line-through;
  color: green;
}
</style>
