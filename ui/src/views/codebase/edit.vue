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

        <!-- top edit bar -->
        <div class="mgb20">
            <div class="flex">
                <el-input v-model="title" style="width: 30%"></el-input>
            </div>
            <!-- edit snippet bar -->
            <div>
                <el-button @click="saveCode">save</el-button>
                <el-button @click="addNewSnippet">+</el-button>
                <el-button @click="removeSnippet">-</el-button>
                <el-button>up</el-button>
                <el-button>down</el-button>
            </div>
        </div>
        <div>{{ selectedSnippetId }}</div>
        <!-- snippets -->
        <div>
            <!-- rows -->
            <div
                :id="formAnIdForSnippetDiv(snippet)"
                @click="focusSnippet($event)"
                style="border: 1px solid"
                v-for="snippet in snippets"
                :key="snippet.id"
                class="flex mgb20"
            >
                <div class="mgr20">id: {{ snippet.id }}</div>

                <div>order: {{ snippet.order }}</div>
                <!-- code -->
                <div>
                    <!-- code edit bar -->

                    <div class="mgr20">
                        <div>
                            <el-select
                                v-model="language"
                                @change="handleOptionChange"
                            >
                                <el-option
                                    value="java"
                                    label="java"
                                ></el-option>
                                <el-option
                                    value="kotlin"
                                    label="kotlin"
                                ></el-option>
                                <el-option
                                    value="javascript"
                                    label="javascript"
                                ></el-option>
                                <el-option
                                    value="cpp"
                                    label="c/cpp"
                                ></el-option>
                                <el-option
                                    value="html"
                                    label="html"
                                ></el-option>
                            </el-select>

                            <el-button @click="runCode">run</el-button>
                        </div>
                        <el-input
                            type="textarea"
                            v-model="snippet.content"
                        ></el-input>
                    </div>
                </div>

                <!-- desc -->
                <div>
                    <el-input
                        type="textarea"
                        v-model="snippet.description"
                    ></el-input>
                </div>
            </div>
        </div>
        <!-- <div class="flex">
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
        </div> -->
    </div>
</template>

<script>
import Tinymce from '@/components/Tinymce'
import loader from '@monaco-editor/loader'
import {
    createSnippet,
    deleteSnippet,
    listSnippetsForArticle,
    updateArticle,
} from '@/api/article'
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
            articleId: this.$route.query.articleId,
            title: '',
            language: 'java',
            monacoeditor: undefined,
            mode: this.$route.query.mode,
            description: '',
            onlineCodeRunningOutput: '',
            snippets: [],
            selectedSnippetId: -1,
        }
    },
    computed: {},
    watch: {},
    async created() {
        this.init()
    },

    methods: {
        // remove the snippet seletedSnippetId points to
        removeSnippet() {
            // check if there is still snippt
            if (this.snippets.length == 0) {
                return
            }

            var snippetId = this.selectedSnippetId

            // find out the index of selected snippet in the array
            var originalPlace = this.snippets.findIndex(
                (snippet) => snippet.id == snippetId,
            )

            var isLast = false

            // determine if this snippet is the last
            if (this.snippets.length > 0) {
                var lastSnippet = this.snippets[this.snippets.length - 1]
                if (lastSnippet.id == snippetId) {
                    isLast = true
                }
            }

            deleteSnippet(snippetId).then((res) => {
                // remove the snippet from the snippets array
                this.snippets = this.snippets.filter(
                    (snippet) => snippet.id != snippetId,
                )

                var orderMap = res.data.orderMap
                // reassign the order of the snippets according to the orderMap returned from the backend
                this.snippets.forEach((snippet) => {
                    snippet.order = orderMap[snippet.id]
                })

                // focus the same place if we just removed the last one snippet
                if (isLast) {
                    // focus on the last one
                    this.focusSnippetBySnippetId(
                        this.snippets[this.snippets.length - 1].id,
                    )
                } else {
                    // focus the next snippet if we just removed the snippet that is not the last one
                    this.focusSnippetBySnippetId(
                        this.snippets[originalPlace].id,
                    )
                }
            })
        },
        getAllSnippetElement() {
            // get all elements whose id starts with "snippet"
            const snippetElements = document.querySelectorAll(
                this.snippetDivSelector(),
            )
            return snippetElements
        },
        getSippetIdFromDivId(divId) {
            return divId.substring(
                divId.indexOf('snippet-') + 'snippet-'.length,
            )
        },
        formAnIdForSnippetDiv(snippet) {
            return `snippet-${snippet.id}`
        },
        snippetDivSelector(snippet) {
            return '[id^="snippet-"]'
        },
        focusSnippet(e) {
            // get snippet id from element, use closest to ensure if you click the nested elements like input or something, you can still get
            // the "snippet-" prefixed div correctly
            var target = e.target.closest(this.snippetDivSelector())

            var selectedSnippetId = this.getSippetIdFromDivId(target.id)

            this.doFocusSnippet(target, selectedSnippetId)
        },

        focusSnippetBySnippetId(selectedSnippetId) {
            // get snippet element by id
            var target = document.getElementById(
                this.formAnIdForSnippetDiv({ id: selectedSnippetId }),
            )
            this.doFocusSnippet(target, selectedSnippetId)
        },

        doFocusSnippet(target, selectedSnippetId) {
            // highlight it
            target.classList.add('snippet-highlight')

            this.selectedSnippetId = selectedSnippetId

            // remove highlight from the rest snippet
            this.reverseHighLightSnippet(selectedSnippetId)
        },

        reverseHighLightSnippet(selectedSnippetId) {
            // remove highlight from the rest snippet
            var snippetElements = this.getAllSnippetElement()
            snippetElements.forEach((element) => {
                // get the part of the ID that follows "snippet"
                const snippetId = this.getSippetIdFromDivId(element.id)
                if (snippetId != selectedSnippetId) {
                    console.log(snippetId) // output: "123"
                    element.classList.remove('snippet-highlight')
                }
            })
        },

        addNewSnippet() {
            // find order of current focused snippet
            var snippets = this.snippets
            var selectedOrder = -1
            snippets.forEach((snippet) => {
                if (snippet.id == this.selectedSnippetId) {
                    selectedOrder = snippet.order
                }
            })

            // we are going to create the first one snippet
            var newOrder = selectedOrder < 0 ? 0 : selectedOrder + 1

            createSnippet(this.articleId, {
                content: '',
                description: '',
                lang: 'kotlin',
                order: newOrder,
            }).then((resp) => {
                var newSnippetHandle = resp.data.newSnippetHandle
                var orderMap = resp.data.orderMap

                // add new snippet to snippets anyway
                this.snippets.push({
                    id: newSnippetHandle,
                    content: '',
                    description: '',
                    lang: 'kotlin',
                    order: newOrder,
                })

                // update the order of all snippets by snippet id according to orderMap
                for (let i = 0; i < snippets.length; i++) {
                    snippets[i].order = orderMap[snippets[i].id]
                }

                // sort snippets by order
                snippets.sort((a, b) => {
                    return a.order - b.order
                })

                // focus on the new snippet
                this.$nextTick(function () {
                    this.focusSnippetBySnippetId(newSnippetHandle)
                })
            })
        },
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
            listSnippetsForArticle(this.articleId).then((resp) => {
                this.snippets = resp.data
                this.snippets.sort((a, b) => a.order - b.order)
                // focus the first snippet
                if (this.snippets.length > 0) {
                    this.$nextTick(function () {
                        this.focusSnippetBySnippetId(this.snippets[0].id)
                    })
                }
            })

            // loader
            //     .init()
            //     .then((monaco) => {
            //         const editorOptions = {
            //             language: 'java',
            //             minimap: { enabled: true },
            //         }
            //         var monacoeditor = monaco.editor
            //         var standaloneeditor = monacoeditor.create(
            //             document.getElementById('editor'),
            //             editorOptions,
            //         )

            //         this.standaloneeditor = standaloneeditor

            //         this.monacoeditor = monacoeditor

            //         if (this.mode == 'edit') {
            //             var snippetId = this.$route.query.snippetId
            //             if (snippetId == undefined) {
            //                 throw Error('page init error')
            //             }

            //             this.snippetId = snippetId
            //             return getSnippetById(snippetId)
            //         }
            //     })
            //     .then((resp) => {
            //         this.id = resp.data.id
            //         this.title = resp.data.title
            //         this.standaloneeditor.setValue(resp.data.codeContent)
            //         this.language = resp.data.lang
            //         this.description = resp.data.description
            //         this.changeLanguage(resp.data.lang)
            //     })
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
                updateArticle(this.id, {
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

.snippet-highlight {
    background-color: azure;
}
</style>
