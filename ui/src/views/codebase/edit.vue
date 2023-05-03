<template>
    <div class="">
        <div class="flex mgb20 mgt20">
            <div class="flex flex-center">
                <a
                    href="#"
                    onclick="history.back(); return false;"
                    style="font-size: 16px"
                >
                    <i class="el-icon-back"></i>

                    Back To Snippets List
                </a>
            </div>
        </div>

        <div>
            <el-input v-model="title"></el-input>
            <el-select v-model="language" @change="handleOptionChange">
                <el-option value="java" label="java"></el-option>
                <el-option value="kotlin" label="kotlin"></el-option>
                <el-option value="javascript" label="javascript"></el-option>
                <el-option value="cpp" label="c/cpp"></el-option>
                <el-option value="html" label="html"></el-option>
            </el-select>
            <el-button @click="saveCode">save</el-button>
            <el-button @click="runCode">run</el-button>
        </div>
        <div class="flex">
            <div>
                <div id="editor" style="width: 500px; height: 500px"></div>
                <div>
                    <el-input
                        type="textarea"
                        :rows="10"
                        placeholder="output"
                        v-model="onlineCodeRunningOutput"
                    ></el-input>
                </div>
            </div>

            <div class="flexg1">
                <Tinymce ref="editor" v-model="description" :height="400" />
            </div>
        </div>
    </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import loader from '@monaco-editor/loader'
import { createSnippet, getSnippetById, updateSnippet } from '@/api/snippets.js'
export default {
    components: { Tinymce },
    beforeRouteLeave(to, from, next) {
        document.removeEventListener('keydown', this.handleKeyDown)
        next()
    },
    beforeRouteEnter(to, from, next) {
        next((vm) => {
            vm.$nextTick(() => {
                document.addEventListener('keydown', vm.handleKeyDown)
            })
        })
    },
    data() {
        return {
            title: '',
            language: 'java',
            monacoeditor: undefined,
            mode: this.$route.query.mode,
            description: '',
            onlineCodeRunningOutput: '',
        }
    },
    computed: {},
    watch: {},
    async created() {
        this.init()
    },

    methods: {
        handleKeyDown(event) {
            // Check if the "Ctrl" key and "S" key were both pressed
            if (event.ctrlKey && event.key === 's') {
                event.preventDefault()
                console.log('Ctrl + S pressed!')
                this.saveCode()
                // Do something else here, such as saving data or triggering an action
            }
        },
        handleOptionChange() {
            this.changeLanguage(this.language)
        },
        runCode() {},
        init() {
            loader
                .init()
                .then((monaco) => {
                    const editorOptions = {
                        language: 'java',
                        minimap: { enabled: true },
                    }
                    var monacoeditor = monaco.editor
                    var standaloneeditor = monacoeditor.create(
                        document.getElementById('editor'),
                        editorOptions,
                    )

                    this.standaloneeditor = standaloneeditor

                    this.monacoeditor = monacoeditor

                    if (this.mode == 'edit') {
                        var snippetId = this.$route.query.snippetId
                        if (snippetId == undefined) {
                            throw Error('page init error')
                        }

                        this.snippetId = snippetId
                        return getSnippetById(snippetId)
                    }
                })
                .then((resp) => {
                    this.id = resp.data.id
                    this.title = resp.data.title
                    this.standaloneeditor.setValue(resp.data.codeContent)
                    this.language = resp.data.lang
                    this.description = resp.data.description
                    this.changeLanguage(resp.data.lang)
                })
        },
        changeLanguage(lang) {
            this.language = lang
            this.monacoeditor.setModelLanguage(
                this.standaloneeditor.getModel(),
                lang,
            )
        },
        saveCode() {
            const code = this.standaloneeditor.getValue()

            if (this.title === '') {
                this.$notify({
                    type: 'error',
                    message: 'title is empty',
                })
                return
            }

            if (this.mode == 'create') {
                createSnippet({
                    title: this.title,
                    codeContent: code,
                    lang: this.language,
                    description: this.description,
                }).then((resp) => {
                    this.$notify({
                        type: 'success',
                        message: 'SUCCESS',
                    })
                })
            } else if (this.mode == 'edit') {
                updateSnippet(this.id, {
                    title: this.title,
                    codeContent: code,
                    lang: this.language,
                    description: this.description,
                }).then((resp) => {
                    this.$notify({
                        type: 'success',
                        message: 'SUCCESS',
                    })
                })
            }
        },
    },
}
</script>
<style lang="scss" scoped>
@import '~@/styles/common-style.scss';
</style>
