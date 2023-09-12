<!--  -->
<template>
    <div class="flex">
        <el-input
            v-model="title"
            placeholder="title"
            @input="handleKeydown"
        ></el-input>
        <el-button @click="search">search</el-button>
    </div>
</template>

<script lang="ts">
import { Component, Vue, Watch } from 'vue-property-decorator';

@Component({})
export default class SearchView extends Vue {
    title: string | null = '';
    lang: string | null = '';
    delay = 100; // Delay in milliseconds
    timeoutId: number | null = null;
    @Watch('$route')
    onRouteChanged(new_route: any) {
        this.populateQueryParam(new_route);
    }

    created() {
        this.populateQueryParam(this.$route);
    }
    handleKeydown() {
        // Clear the previous timeout (if any)
        if (this.timeoutId != null) {
            clearTimeout(this.timeoutId);
        }

        // Set a new timeout
        this.timeoutId = setTimeout(() => {
            // Perform some action here
            console.log(`User typed: ${this.title}`);
            this.search();
        }, this.delay);
    }

    populateQueryParam(route: any) {
        var query = route.query;
        var title = query.title;
        if (title != undefined) {
            this.title = title;
        }
    }

    search() {
        if (this.title === '') {
            this.title = null;
        }

        if (this.lang === '') {
            this.lang = null;
        }

        this.$emit('search-hit', { title: this.title, lang: this.lang });
    }
}
</script>
<style lang="scss" scoped>
@import '~@/style/common-style.scss';
</style>
