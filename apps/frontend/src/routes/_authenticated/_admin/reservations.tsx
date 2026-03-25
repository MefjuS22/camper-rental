import { createFileRoute, redirect } from "@tanstack/react-router";
import { useTranslation } from "react-i18next";
import { Stack, Typography } from "@mui/material";

export const Route = createFileRoute("/_authenticated/_admin/reservations")({
  beforeLoad: ({ context }) => {
    const permissions = context.auth?.permissions ?? [];
    if (!permissions.includes("RESERVATIONS_ADMIN")) {
      throw redirect({ to: "/", replace: true });
    }
  },
  component: ReservationsPage,
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
