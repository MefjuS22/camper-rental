import { createRouter } from "@tanstack/react-router";
import type { JwtResponseDto } from "@camper-rent/api-client";

import { routeTree } from "./routeTree.gen";

export type RouterContext = {
  auth: JwtResponseDto | null;
};

export const router = createRouter({
  routeTree,
  context: {
    auth: undefined!
  } satisfies RouterContext
});

declare module "@tanstack/react-router" {
  interface Register {
    router: typeof router;
  }
}
