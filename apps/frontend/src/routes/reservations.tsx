import { createFileRoute } from "@tanstack/react-router";
import { useTranslation } from "react-i18next";
import { Stack, Typography } from "@mui/material";

export const Route = createFileRoute("/reservations")({
  component: ReservationsPage
});

function ReservationsPage() {
  const { t } = useTranslation();

  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={600}>
        {t("reservations.title")}
      </Typography>
      <Typography variant="body1" color="text.secondary">
        {t("reservations.placeholder")}
      </Typography>
    </Stack>
  );
}
