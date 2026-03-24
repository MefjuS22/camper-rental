import { createFileRoute } from "@tanstack/react-router";
import { Stack, Typography } from "@mui/material";

export const Route = createFileRoute("/reservations")({
  component: ReservationsPage
});

function ReservationsPage() {
  return (
    <Stack spacing={2}>
      <Typography variant="h5" fontWeight={600}>
        Rezerwacje
      </Typography>
      <Typography variant="body1" color="text.secondary">
        Moduł rezerwacji — wkrótce lista i obsługa rezerwacji.
      </Typography>
    </Stack>
  );
}
