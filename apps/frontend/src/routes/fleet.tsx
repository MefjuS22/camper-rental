import { createFileRoute } from "@tanstack/react-router";
import { Stack, Typography } from "@mui/material";

export const Route = createFileRoute("/fleet")({
  component: FleetPage
});

function FleetPage() {
  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={600}>
        Flota
      </Typography>
      <Typography variant="body1" color="text.secondary">
        Moduł floty — wkrótce lista kamperów i zarządzanie pojazdami.
      </Typography>
    </Stack>
  );
}
