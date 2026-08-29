<!-- Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ -->
<script setup>
import {onMounted,reactive,ref} from 'vue'
import {request} from '../api'
const docs=ref([]),error=ref('')
const hex=()=>Array.from(crypto.getRandomValues(new Uint8Array(32)),b=>b.toString(16).padStart(2,'0')).join('')
const form=reactive({documentNo:'DOC-'+Date.now(),name:'企业管理制度.pdf',owner:'行政部',classification:'INTERNAL',sizeBytes:2048,changeNote:'初始版本'})
async function load(){docs.value=await request('/api/core/drive/documents')}
async function run(fn){try{error.value='';await fn();await load()}catch(e){error.value=e.message}}
async function create(){await run(async()=>{await request('/api/core/drive/documents',{method:'POST',body:JSON.stringify({...form,checksum:hex()})});form.documentNo='DOC-'+Date.now()})}
async function action(d,type){await run(()=>{if(type==='checkout')return request(`/api/core/drive/documents/${d.id}/checkout`,{method:'POST',body:JSON.stringify({expiresInHours:8})});if(type==='checkin')return request(`/api/core/drive/documents/${d.id}/checkin`,{method:'POST',body:JSON.stringify({checksum:hex(),sizeBytes:4096,changeNote:'签出修订后签入'})});if(type==='unlock')return request(`/api/admin/core/drive/documents/${d.id}/force-unlock`,{method:'POST'});return request(`/api/core/drive/documents/${d.id}/versions`,{method:'POST',body:JSON.stringify({checksum:hex(),sizeBytes:4096,changeNote:'工作台修订版本'})})})}
async function share(d){try{const s=await request(`/api/admin/core/drive/documents/${d.id}/shares`,{method:'POST',body:JSON.stringify({expiresInHours:24,maxDownloads:5,password:['CONFIDENTIAL','SECRET'].includes(d.classification)?'demo-pass':''})});alert(`安全共享令牌：${s.token}`)}catch(e){error.value=e.message}}
onMounted(load)
</script>
<template>
  <section class="head"><span>CONTROLLED CONTENT</span><h3>文档签出与安全共享空间</h3><p>版本哈希、签出锁、受控签入与限时共享共同防止并发覆盖和敏感内容扩散。</p></section>
  <p v-if="error" class="err">{{error}}</p>
  <form class="create" @submit.prevent="create"><input v-model="form.documentNo"><input v-model="form.name"><input v-model="form.owner"><select v-model="form.classification"><option>PUBLIC</option><option>INTERNAL</option><option>CONFIDENTIAL</option><option>SECRET</option></select><button>上传首版</button></form>
  <section class="cards"><article v-for="d in docs" :key="d.id"><div class="file">DOC</div><div><code>{{d.documentNo}}</code><h4>{{d.name}}</h4><p>{{d.owner}} · {{d.classification}} · V{{d.currentVersion}}</p><small v-if="d.checkedOut">由 {{d.checkedOutBy}} 签出，至 {{d.checkoutExpiresAt?.replace('T',' ').slice(0,16)}}</small></div><span :class="{locked:d.checkedOut}">{{d.checkedOut?'已锁定':d.status}}</span><div class="actions"><button v-if="!d.checkedOut" @click="action(d,'checkout')">签出编辑</button><button v-if="d.checkedOut" @click="action(d,'checkin')">签入新版本</button><button v-if="d.checkedOut" class="warn" @click="action(d,'unlock')">强制解锁</button><button v-if="!d.checkedOut" @click="action(d,'version')">快速版本</button><button @click="share(d)">安全共享</button></div></article></section>
</template>
<style scoped>
.head,.create,.cards{background:#fff;border:1px solid #dce2df;margin-top:20px;padding:24px}.head span{font-size:11px;letter-spacing:.15em;color:#a56c24}.head h3{margin:6px 0}.head p{margin:0;color:#68777c}.create{display:grid;grid-template-columns:1fr 2fr 1fr 1fr auto;gap:10px}.create input,.create select{padding:10px;border:1px solid #ccd6d2}.create button,.cards button{border:0;background:#235a74;color:#fff;padding:9px 12px}.cards article{display:grid;grid-template-columns:54px 1fr 100px minmax(330px,auto);gap:15px;align-items:center;padding:14px 0;border-top:1px solid #edf0ef}.file{height:54px;display:grid;place-items:center;background:#edf2f0;color:#235a74;font-weight:800}.cards h4{margin:4px 0}.cards p{margin:0;color:#68777c}.cards small{color:#a56c24}.cards button{margin:3px}.actions{text-align:right}.warn,.locked{background:#7d5a52!important;color:#fff;padding:5px 8px}.err{color:#a03f38}@media(max-width:900px){.create,.cards article{grid-template-columns:1fr}.actions{text-align:left}}
</style>
