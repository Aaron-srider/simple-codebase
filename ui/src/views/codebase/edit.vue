<template>
    <div class="">
        <div>
            <el-input v-model="title"></el-input>
            <el-button @click="changeLanguage('javascript')">js</el-button>
            <el-button @click="changeLanguage('java')">java</el-button>
            <el-button @click="saveCode">save</el-button>
        </div>
        <div class="flex">
            <div id="editor" style="width: 500px; height: 500px"></div>
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
    data() {
        return {
            title: '',
            language: 'javascript',
            monacoeditor: undefined,
            mode: this.$route.query.mode,
            description: '',
        }
    },
    computed: {},
    watch: {},
    async created() {
        this.init()
        if (this.mode == 'edit') {
            var snippetId = this.$route.query.snippetId
            if (snippetId == undefined) {
                throw Error('page init error')
            }

            this.snippetId = snippetId
            getSnippetById(snippetId).then((resp) => {
                this.id = resp.data.id
                this.title = resp.data.title
                this.standaloneeditor.setValue(resp.data.codeContent)
                this.language = resp.data.lang
                this.changeLanguage(resp.data.lang)
            })
        }
    },

    methods: {
        async init() {
            loader.init().then((monaco) => {
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
