// @ts-nocheck
import { defineConfig } from "@kubb/core";
import { pluginOas } from "@kubb/plugin-oas";
import { pluginTs } from "@kubb/plugin-ts";
import { pluginZod } from "@kubb/plugin-zod";
import { pluginReactQuery } from "@kubb/plugin-react-query";

const openApiInput =
  process.env.KUBB_OPENAPI_URL ??
  process.env.KUBB_OPENAPI_PATH ??
  "../../apps/backend/src/main/resources/openapi/openapi.yaml";
console.log(openApiInput);
const config = defineConfig({
  input: {
    path: openApiInput
  },
  output: {
    path: "./src/generated",
    clean: true
  },
  plugins: [
    pluginOas(),
    pluginTs({
      output: {
        path: "./models"
      }
    }),
    pluginZod({
      output: {
        path: "./zod"
      }
    }),
    pluginReactQuery({
      output: {
        path: "./hooks"
      }
    })
  ]
});

export default config;
