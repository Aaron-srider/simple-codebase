<template>
    <div class="mgb20 bc-b-gray bc-t-gray bg-gray" style="height: 60px">
        <div
            class="pdt10 pdb10"
            style="width: 95%; height: 100%; margin: 0 auto 0 auto"
        >
            <slot>
                <span class="fs18" style="line-height: 2.2">{{ title }}</span>
            </slot>
        </div>
    </div>
</template>

<script lang="ts">
import { Component, Prop, Vue } from 'vue-property-decorator';
@Component({})
export default class PageFooterComponent extends Vue {
    @Prop({ type: String, default: '' })
    private readonly title!: string;
}
</script>
