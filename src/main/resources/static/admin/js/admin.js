function confirmDeleteTour() {
    return window.confirm(
        "Bạn chắc chắn muốn xóa tour này?"
    );
}

document.addEventListener("DOMContentLoaded", () => {

    const container =
        document.getElementById("itinerary-container");

    const addButton =
        document.getElementById("add-itinerary-btn");

    const template =
        document.getElementById("itinerary-row-template");

    if (!container || !addButton || !template) {
        return;
    }

    const reindex = () => {

        const rows =
            container.querySelectorAll(".itinerary-row");

        rows.forEach((row, index) => {

            row.dataset.index = index;

            const header =
                row.querySelector(".itinerary-title");

            if (header) {
                header.textContent =
                    `Ngày ${index + 1}`;
            }

            const dayNumber =
                row.querySelector(
                    '[data-field="dayNumber"]'
                );

            const title =
                row.querySelector(
                    '[data-field="title"]'
                );

            const description =
                row.querySelector(
                    '[data-field="description"]'
                );

            if (dayNumber) {
                dayNumber.name =
                    `itineraries[${index}].dayNumber`;

                if (!dayNumber.value) {
                    dayNumber.value =
                        index + 1;
                }
            }

            if (title) {
                title.name =
                    `itineraries[${index}].title`;
            }

            if (description) {
                description.name =
                    `itineraries[${index}].description`;
            }

        });
    };

    addButton.addEventListener(
        "click",
        () => {

            const fragment =
                template.content.cloneNode(true);

            container.appendChild(fragment);

            reindex();
        }
    );

    container.addEventListener(
        "click",
        event => {

            const removeButton =
                event.target.closest(
                    ".remove-itinerary-btn"
                );

            if (!removeButton) {
                return;
            }

            removeButton
                .closest(".itinerary-row")
                ?.remove();

            reindex();
        }
    );

    reindex();
});
