export function localGet (key) {
  const value = window.localStorage.getItem(key)
  try {
    return JSON.parse(window.localStorage.getItem(key))
  } catch (error) {
    return value
  }
}

export function localSet (key, value) {
  window.localStorage.setItem(key, JSON.stringify(value))
}

export function localRemove (key) {
  window.localStorage.removeItem(key)
}

// 判断内容是否含有表情字符，现有数据库不支持。
export function hasEmoji (str = '') {
  const reg = /[^\u0020-\u007E\u00A0-\u00BE\u2E80-\uA4CF\uF900-\uFAFF\uFE30-\uFE4F\uFF00-\uFFEF\u0080-\u009F\u2000-\u201f\u2026\u2022\u20ac\r\n]/g;
  return str.match(reg) && str.match(reg).length
}

export const pathMap = {
  login: '登录',
  index: '首页',
  mycharts: '统计图表',
  references: '文献管理',
  file_upload: '文件操作',
  stu_manage: '学生管理',
  account: '修改账户',
  table: '表格练习',
  file: '文件上传练习',
  chart: '图表练习',
  gettable: '表格练习',
  dataset_manage: '数据集',
  datasetupload: '数据集上传',
  user_manage:"用户管理",
  datasetedit: '数据集修改',
  datasetDetail: '数据集详情',
  code_manage: '代码',
  literature_manage: '文献资料',
  codeupload: '代码上传',
  literatureupload: '文献资料上传',
  studentvideo: '教学视频',
  codeDetail: '代码详情',
  codeedit: '代码修改',
  literatureedit: '文献资料修改',
  literatureDetail: '文献资料详情',
  videoupload: '教学视频上传',
  video_manage: '教学视频',
  videoDetail: '教学视频详情',
  videoedit: '教学视频修改',
  videocontentedit: '教学视频修改',
  videocontentupload: '教学视频上传',

}
