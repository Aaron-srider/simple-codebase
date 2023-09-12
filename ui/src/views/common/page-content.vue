<template>
    <div>
        <div class="" style="width: 95%; margin: 20px auto 0 auto">
            <slot></slot>
        </div>
    </div>
</template>
<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
@Component({})
export default class PageContentComponent extends Vue {}
</script>
