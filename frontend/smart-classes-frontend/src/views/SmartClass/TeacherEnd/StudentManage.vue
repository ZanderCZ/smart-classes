<script setup lang="tsx">
import { ContentWrap } from '@/components/ContentWrap'
import { useI18n } from '@/hooks/web/useI18n'
import { Table, TableColumn } from '@/components/Table'
import {
  getStudentListApi,
} from '@/api/student'
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Icon } from '@/components/Icon'
import { BaseButton } from '@/components/Button'
import EditStudent from './EditStudent.vue'
import { useRoute } from 'vue-router'
import { getAssociatedByCidApi, updateClassRecordApi } from '@/api/studentClasses'
import { StudentClasses, StudentClassesUpdateDTO } from '@/api/studentClasses/types'

const route = useRoute()

// 从路由中获取课程 ID
const classId = Number(route.query.classId)

const { t } = useI18n()

const columns: TableColumn[] = [
  {
    field: 'student.id',
    label: t('student.id')
  },
  {
    field: 'student.name',
    label: t('student.name')
  },
  {
    field: 'student.gender',
    label: t('student.gender')
  },
  {
    field: 'student.department.name',
    label: t('student.dept'),
    slots: {
      default: ({ row }) => row.student.department?.name || '未知'
    }
  },
  {
    field: 'grade',
    label: '成绩',
    formatter: (row) => {
      return row.grade ?? 0 // 如果是 null 或 undefined，就返回 0
    }
  },
  {
    field: 'action',
    label: t('tableDemo.action'),
    slots: {
      default: (data) => {
        return (
          <BaseButton type="primary" onClick={() => editFn(data.row)}>
            编辑
          </BaseButton>
        )
      }
    }
  }
]

const loading = ref(true)
const tableDataList = ref<StudentClasses[]>([])

const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const getTableList = async () => {
  if (!classId) {
    ElMessage.warning('请提供课程ID')
    return
  }

  loading.value = true

  try {
    const res = await getAssociatedByCidApi(classId)

    if (res.code === 200 && Array.isArray(res.data)) {
      tableDataList.value = res.data
      total.value = res.data.length

      console.log('选课学生列表：', tableDataList.value)
    } else {
      ElMessage.error('获取学生列表失败')
    }
  } catch (error) {
    console.error('获取学生列表异常', error)
    ElMessage.error('获取学生列表出错')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getTableList()
})

// 搜索相关状态
const searchKeyword = ref('')
const isSearching = ref(false)

const editDialogVisible = ref(false)
const editData = ref({})

const editFn = (data) => {
  editData.value = { ...data }
  editDialogVisible.value = true
}

const onEditSave = async (newData) => {
  const updateDto: StudentClassesUpdateDTO = {
    id: newData.id,
    cid: newData.classes.id,
    sid: newData.student.id,
    grade: newData.grade
  }

  try {
    await updateClassRecordApi(updateDto)
    ElMessage.success('保存成功')
    getTableList() // 刷新表格
  } catch (e) {
    ElMessage.error('保存失败')
  }
}

const onDeleted = () => {
  getTableList()
}

// 分页事件处理
const handleCurrentChange = (page: number) => {
  currentPage.value = page
  getTableList()
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  getTableList()
}

// 搜索姓名 - 改用前端过滤方式
const searchFn = async () => {
  if (!searchKeyword.value.trim()) {
    ElMessage.warning('请输入搜索关键词')
    return
  }

  try {
    isSearching.value = true
    loading.value = true

    console.log('开始搜索，关键词:', searchKeyword.value.trim())

    const res = await getStudentListApi({
      pageIndex: 1,
      pageSize: 1000 
    })

    console.log('获取所有学生数据:', res)

    if (res && res.data) {
      const allStudents = Array.isArray(res.data) ? res.data : res.data.list || []
      console.log('所有学生数据:', allStudents)
      const keyword = searchKeyword.value.trim().toLowerCase()
      const filteredStudents = allStudents.filter(
        (student) => student.name && student.name.toLowerCase().includes(keyword)
      )

      console.log('过滤后的学生:', filteredStudents)

      if (filteredStudents.length > 0) {
        tableDataList.value = filteredStudents
        total.value = filteredStudents.length
        currentPage.value = 1
        ElMessage.success(`找到 ${filteredStudents.length} 条匹配数据`)
      } else {
        tableDataList.value = []
        total.value = 0
        ElMessage.info('未找到匹配的学生')
      }
    } else {
      tableDataList.value = []
      total.value = 0
      ElMessage.error('获取学生数据失败')
    }
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败，请稍后重试')
    tableDataList.value = []
    total.value = 0
  } finally {
    loading.value = false
    isSearching.value = false
  }
}

// 重置搜索，显示所有数据
const resetSearch = () => {
  searchKeyword.value = ''
  isSearching.value = false
  currentPage.value = 1
  getTableList()
}
</script>

<template>
  <ContentWrap>
    <div style="display: flex; align-items: center; gap: 16px; margin-bottom: 16px">
      <span style="font-size: 18px; font-weight: bold">{{ t('teacher.studentManage') }}</span>
      <el-button @click="() => getTableList()" style="margin-left: 30px">刷新</el-button>
    </div>
    <div style="display: flex; align-items: center; gap: 16px; margin-bottom: 16px">
      <span style="font-size: 18px; font-weight: bold"></span>
      <el-input
        v-model="searchKeyword"
        placeholder="输入姓名搜索"
        @keyup.enter="searchFn"
        clearable
        style="width: 400px; margin-left: 0px"
      >
        <template #prefix>
          <Icon icon="ep:search" />
        </template>
      </el-input>
      <div style="display: flex; gap: 8px">
        <el-button type="primary" @click="searchFn" :loading="isSearching" plain> 搜索 </el-button>
        <el-button type="info" @click="resetSearch" plain>重置</el-button>
      </div>
    </div>
    <Table
      :columns="columns"
      :data="tableDataList"
      :loading="loading"
      :defaultSort="{ prop: 'display_time', order: 'descending' }"
    />
    <EditStudent
      v-model:visible="editDialogVisible"
      :data="editData"
      @save="onEditSave"
      @deleted="onDeleted"
    />
    <div style="display: flex; justify-content: center; margin-top: 16px">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        :hide-on-single-page="false"
        @current-change="handleCurrentChange"
        @size-change="handleSizeChange"
      />
    </div>
  </ContentWrap>
</template>
