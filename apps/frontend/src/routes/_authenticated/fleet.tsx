import { createFileRoute } from "@tanstack/react-router";
import { useTranslation } from "react-i18next";
import { Stack, Typography } from "@mui/material";

export const Route = createFileRoute("/_authenticated/fleet")({
  component: FleetPage
});

function FleetPage() {
  const { t } = useTranslation();

  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={600}>
        {t("fleet.title")}
      </Typography>
      <Typography variant="body1" color="text.secondary">
        {t("fleet.placeholder")}
      </Typography>
    </Stack>
  );
}
